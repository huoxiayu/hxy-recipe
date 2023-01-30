package com.hxy.recipe.agent;

import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Scanner;

@Slf4j
public class VMAgent {

    public static void main(String[] args) {
        log.info("process list");
        List<VirtualMachineDescriptor> virtualMachineDescriptorList = VirtualMachine.list();
        for (VirtualMachineDescriptor vmd : virtualMachineDescriptorList) {
            log.info("id [{}] => [{}]", vmd.id(), vmd.displayName());
        }

        Scanner in = new Scanner(System.in);
        String line;
        while (!"end".equals(line = in.nextLine())) {
            try {
                VirtualMachine virtualMachine = VirtualMachine.attach(line);
                log.info("attach -> [{}]", virtualMachine.id());
                // biz operation
                virtualMachine.detach();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}
