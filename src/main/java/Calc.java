import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.CodePointBuffer;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.nio.CharBuffer;
import java.util.Stack;

@Slf4j
public class Calc {

    private void compute(String expression) {
        // 根据表达式创建lexer
        CharBuffer charBuffer = CharBuffer.wrap(expression.toCharArray());
        CodePointBuffer cpb = CodePointBuffer.withChars(charBuffer);
        CalcLexer lexer = new CalcLexer(CodePointCharStream.fromBuffer(cpb));

        // 根据lexer创建token-stream
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        // 根据token-stream创建parser
        CalcParser parser = new CalcParser(tokens);

        // 为parser添加一个监听器
        ComputeListener listener = new ComputeListener();
        parser.addParseListener(listener);

        parser.start();

        log.info("{}", listener.getStack());
        log.info("{} = {}", expression, listener.getStack().pop());
    }

    @Getter
    public static class ComputeListener extends CalcBaseListener {

        private final Stack<Double> stack = new Stack<>();

        @Override
        public void enterAddOrSub(CalcParser.AddOrSubContext ctx) {
            log.info("enterAddOrSub -> {}", ctx.getText());
        }

        @Override
        public void exitAddOrSub(CalcParser.AddOrSubContext ctx) {
            log.info("exitAddOrSub -> {}", ctx.getText());
            Double s1 = stack.pop();
            Double s2 = stack.pop();
            String op = ctx.getChild(1).getText();
            if ("+".equals(op)) {
                stack.push(s1 + s2);
            } else if ("-".equals(op)) {
                stack.push(s1 - s2);
            } else {
                throw new IllegalStateException("unreachable code");
            }
        }

        @Override
        public void enterNumber(CalcParser.NumberContext ctx) {
            log.info("enterNumber -> {}", ctx.getText());
        }

        @Override
        public void exitNumber(CalcParser.NumberContext ctx) {
            log.info("exitNumber -> {}", ctx.getText());
            stack.push(Double.valueOf(ctx.getText()));
        }

        @Override
        public void enterMulOrDiv(CalcParser.MulOrDivContext ctx) {
            log.info("enterMulOrDiv -> {}", ctx.getText());
        }

        @Override
        public void exitMulOrDiv(CalcParser.MulOrDivContext ctx) {
            log.info("exitMulOrDiv -> {}", ctx.getText());
            Double s1 = stack.pop();
            Double s2 = stack.pop();
            String op = ctx.getChild(1).getText();
            if ("*".equals(op)) {
                stack.push(s1 * s2);
            } else if ("/".equals(op)) {
                stack.push(s1 / s2);
            } else {
                throw new IllegalStateException("unreachable code");
            }
        }

        @Override
        public void visitTerminal(TerminalNode node) {
            log.info("visitTerminal -> {}", node.getSymbol().getText());
        }

        @Override
        public void visitErrorNode(ErrorNode node) {
            throw new IllegalStateException("unreachable code");
        }
    }

    public static void main(String[] args) {
        Calc calc = new Calc();
        String expression = "1 + 2 * 3 + 4 + 5 * 6";
        calc.compute(expression);
    }

}
