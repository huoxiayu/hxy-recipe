#include <cstdio>
#include <cstdlib>
#include <cstring>
#include <string>

const static uint32_t BUF_SIZE = 1024;

struct ShellCmd {
  public:
    ShellCmd(std::string cmd_, std::string &ret_result_, int &ret_code_)
        : cmd(cmd_), ret_result(ret_result_), ret_code(ret_code_) {
        ret_code = -1;
    }

    ShellCmd(const ShellCmd &) = delete;

    ShellCmd &operator=(const ShellCmd &) = delete;

    ShellCmd(ShellCmd &&) = delete;

    ShellCmd &operator=(ShellCmd &&) = delete;

    void execute() {
        fp = popen(cmd.c_str(), "r");
        if (fp) {
            char buf[BUF_SIZE] = {0};
            while (fgets(buf, sizeof(buf), fp) != nullptr) {
                ret_result.append(buf);
                memset(buf, 0, sizeof(buf));
            }
        }
    }

    ~ShellCmd() {
        if (fp) {
            ret_code = pclose(fp);
        }
    }

  private:
    std::string cmd;
    std::string &ret_result;
    int &ret_code;
    FILE *fp = nullptr;
};

int main() {
    std::string ret_result;
    int ret_code;
    ShellCmd("ls && echo '-----------' && cat *.cpp", ret_result, ret_code)
        .execute();
    printf("%s\n", ret_result.c_str());
    printf("********************\n");
    printf("%d\n", ret_code);
    return 0;
}
