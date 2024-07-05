package com.example.hellojadx;

import java.io.File;

import jadx.api.JadxArgs;
import jadx.api.JadxDecompiler;
import jadx.api.JavaClass;
import jadx.api.JavaMethod;
import jadx.api.impl.SimpleCodeWriter;
import jadx.core.dex.instructions.InvokeNode;
import jadx.core.dex.nodes.BlockNode;
import jadx.core.dex.nodes.InsnNode;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        String path = args[0];
        // String to_find = args[1];
        // jadx api 解析APK
        JadxArgs jadxArgs = new JadxArgs();
        jadxArgs.setInputFile(new File(path));
        jadxArgs.setOutDir(new File("output"));
        jadxArgs.setCodeWriterProvider(SimpleCodeWriter::new);
        try (JadxDecompiler jadx = new JadxDecompiler(jadxArgs)) {
            jadx.load();
            jadx.save();
            for (JavaClass cls : jadx.getClasses()) {
                for (JavaMethod mth : cls.getMethods()) {
                    if (mth.getMethodNode().getBasicBlocks() == null) { continue; }
                    for (BlockNode block : mth.getMethodNode().getBasicBlocks()) {
                        for(InsnNode a: block.getInstructions()) {
                            if (a instanceof InvokeNode) {
                                InvokeNode invokeNode = (InvokeNode) a;
                                String name = invokeNode.getCallMth().getName();
                                String clazz = invokeNode.getCallMth().getDeclClass().getFullName();
                                System.out.println("调用方法：" + clazz + "." + name);
                                // if ((clazz+'.'+name).equals(to_find)) {
                                //     System.out.println(mth.getFullName() + "调用了想找的方法");
                                // }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
