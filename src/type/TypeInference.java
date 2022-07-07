package edu.kit.joana.api.example;
import java.io.File;
import java.io.FileWriter;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;

import com.ibm.wala.ipa.cha.ClassHierarchyException;
import com.ibm.wala.util.CancelException;
import com.ibm.wala.util.NullProgressMonitor;
import com.ibm.wala.util.graph.GraphIntegrity.UnsoundGraphException;

import edu.kit.joana.api.IFCAnalysis;
import edu.kit.joana.api.IFCType;
import edu.kit.joana.api.lattice.BuiltinLattices;
import edu.kit.joana.api.sdg.SDGConfig;
import edu.kit.joana.api.sdg.SDGProgram;
import edu.kit.joana.api.sdg.SDGProgramPart;
import edu.kit.joana.api.sdg.SDGBuildPreparation;
import edu.kit.joana.ifc.sdg.core.SecurityNode;
import edu.kit.joana.ifc.sdg.core.violations.IViolation;
import edu.kit.joana.ifc.sdg.graph.SDGSerializer;
import edu.kit.joana.ifc.sdg.graph.SDGNode;
import edu.kit.joana.ifc.sdg.graph.SDGEdge;
import edu.kit.joana.ifc.sdg.graph.SDG;
import edu.kit.joana.ifc.sdg.mhpoptimization.MHPType;
import edu.kit.joana.ifc.sdg.util.JavaMethodSignature;
import edu.kit.joana.util.Stubs;
import edu.kit.joana.wala.core.SDGBuilder.ExceptionAnalysis;
import edu.kit.joana.wala.core.SDGBuilder.PointsToPrecision;
import edu.kit.joana.wala.core.SDGBuilder.FieldPropagation;
import gnu.trove.map.TObjectIntMap;

public class TypeInference {
    public static void main(String[] args) throws ClassHierarchyException, IOException, UnsoundGraphException, CancelException {
            /** the class path is either a directory or a jar containing all the classes of the program which you want to analyze */
            String classPath = args[0]+"/"+args[1];

            // file that record other information
            File info = new File(args[0]+"/info.txt");

            info.createNewFile();
            FileWriter info_fileWriter = new FileWriter(info.getAbsoluteFile());
            BufferedWriter info_bw = new BufferedWriter(info_fileWriter);

            // record console output
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(baos);
            PrintStream old = System.out;
            System.setOut(ps);

            /** search for entry methods */
            /** the entry method is the main method which starts the program you want to analyze */
            SDGBuildPreparation.Config cfg = new SDGBuildPreparation.Config("Search main <unused>", "unused", classPath, true, FieldPropagation.FLAT);
            List<String> res = SDGBuildPreparation.searchMethods(System.out, cfg, false, ".*");
            if (res.isEmpty()) {
                System.out.println("no entry method found!\n");
            }
            JavaMethodSignature entryMethod = JavaMethodSignature.fromString(res.get(Integer.parseInt(args[2])));


            // record method finding info
            System.setOut(old);
            System.out.println(baos.toString());
            System.setOut(ps);
            info_bw.write(baos.toString());
            info_bw.write("Methods found: " + res.size() + "\n");
            baos.reset();

            info_bw.write("\n######################################################\n\n");

            /** For multi-threaded programs, it is currently neccessary to use the jdk 1.5 stubs */
            SDGConfig config = new SDGConfig(classPath, entryMethod.toBCString(), Stubs.JRE_15);
        
            /** compute interference edges to model dependencies between threads (set to false if your program does not use threads) */
            config.setComputeInterferences(true);
        
            /** additional MHP analysis to prune interference edges (does not matter for programs without multiple threads) */
            config.setMhpType(MHPType.PRECISE);
        
            /** precision of the used points-to analysis - INSTANCE_BASED is a good value for simple examples */
            config.setPointsToPrecision(PointsToPrecision.INSTANCE_BASED);
        
            /** exception analysis is used to detect exceptional control-flow which cannot happen */
            config.setExceptionAnalysis(ExceptionAnalysis.INTERPROC);

            // config.setIgnoreIndirectFlows(true);

            /** build the PDG */
            SDGProgram program = SDGProgram.createSDGProgram(config, System.out, new NullProgressMonitor());

            // record sdg building info and close record
            System.out.flush();
            System.setOut(old);
            System.out.println(baos.toString());
            info_bw.write(baos.toString());

            /** save PDG to disk */ 
            SDGSerializer.toPDGFormat(program.getSDG(), new FileOutputStream(args[0]+"/SDGFile.pdg"));

            // /** load sdg through pdg file */
            // String pdgPath = "/home/andres/benchmarks/blazer_modpow1/SDGFile.pdg";
            // SDGProgram program = SDGProgram.loadSDG(pdgPath, MHPType.PRECISE);

            /** init files and writers */
            File if_output = new File(args[0]+"/if_branch.txt");


            if_output.createNewFile();
            FileWriter if_fileWriter = new FileWriter(if_output.getAbsoluteFile());
            BufferedWriter if_bw = new BufferedWriter(if_fileWriter);

            File if_prev_output = new File(args[0]+"/if_branch_prev.txt");

            if_prev_output.createNewFile();
            FileWriter if_prev_fileWriter = new FileWriter(if_prev_output.getAbsoluteFile());
            BufferedWriter if_prev_bw = new BufferedWriter(if_prev_fileWriter);

            File call_output = new File(args[0]+"/call.txt");

            call_output.createNewFile();
            FileWriter call_fileWriter = new FileWriter(call_output.getAbsoluteFile());
            BufferedWriter call_bw = new BufferedWriter(call_fileWriter);

            /** taint analysis */
            IFCAnalysis ana = new IFCAnalysis(program);
            /** annotate sources and sinks */
            
            for (int i=3; i<args.length; i++) {
                ana.addSourceAnnotation(program.getPart(args[i]), BuiltinLattices.STD_SECLEVEL_HIGH); 
            }


            for (SDGNode n : program.getSDG().vertexSet()) {
                if (n.getKind() == SDGNode.Kind.PREDICATE) {
                    if_prev_bw.write(n.getLabel()+'\n');
                    if_prev_bw.write(n.getBytecodeName()+'\n');
                    if_prev_bw.write(""+n.getBytecodeIndex()+'\n');
                    ana.addSinkAnnotation(program.getPart(n.getBytecodeName()+(":"+n.getBytecodeIndex())), BuiltinLattices.STD_SECLEVEL_LOW);
                }
            }
            List<String> call_methods = new ArrayList<String>();
            List<String> branch_added = new ArrayList<String>();

            // record taint analysis time
            long startTime = System.currentTimeMillis();

            /** run the analysis */
            Collection<? extends IViolation<SecurityNode>> result = ana.doIFC();

            long endTime = System.currentTimeMillis();
            System.out.println("Taint analysis takes: " + (endTime - startTime) + " ms.");
            info_bw.write("\n######################################################\n\n");
            info_bw.write("Taint analysis takes: " + (endTime - startTime) + " ms.");


            for (IViolation<SecurityNode> r : result) {
                System.out.println(r);
                /** get the illegal sink's sdg node number */
                String str = ""+r;
                String sdgNum = "";
                int i = str.indexOf(",");
                for (int j = i;; j--) {
                    if (str.charAt(j) == ' ') {
                        sdgNum = str.substring(j+1,i);
                        break;
                    }
                }
                SDGNode n = program.getSDG().getNode(Integer.parseInt(sdgNum));
                /** recode those if branches that involve secret */
                if (branch_added.contains(n.getLabel())) {
                    continue;
                }
                branch_added.add(n.getLabel());
                if_bw.write(n.getLabel()+'\n');
                if_bw.write(n.getBytecodeName()+'\n');
                if_bw.write(""+n.getBytecodeIndex()+'\n');
                /** go through branch to record methods */
                for (SDGEdge e : program.getSDG().outgoingEdgesOf(n)) {
                    SDGNode branch_node = e.getTarget();
                    // record call in branches
                    if (branch_node.getKind() == SDGNode.Kind.CALL) {
                        Collection<SDGNode> possTgts = program.getSDG().getPossibleTargets(branch_node);
                        if (possTgts.isEmpty()) {
                            String unresolvedCallTarget = branch_node.getUnresolvedCallTarget();
                            if (unresolvedCallTarget != null && !call_methods.contains(unresolvedCallTarget)) {
                                call_bw.write(unresolvedCallTarget +'\n');
                                call_methods.add(unresolvedCallTarget);
                            }
                        }
                        else {
                            for (SDGNode entry : possTgts) {
                                if (!call_methods.contains(entry.getBytecodeName())) {
                                    call_bw.write(entry.getBytecodeName()+'\n');
                                    call_methods.add(entry.getBytecodeName());
                                }
                            }
                        }
                        // record compound call if exists
                        SDGNode nextNode = program.getSDG().getNode(branch_node.getId()+1);
                        while (true) {
                            // skip all act_in act_out node
                            while (nextNode != null && (nextNode.getKind() == SDGNode.Kind.ACTUAL_IN || nextNode.getKind() == SDGNode.Kind.ACTUAL_OUT)) {
                                nextNode = program.getSDG().getNode(nextNode.getId()+1);
                            }
                            // meet compound call node
                            if (nextNode != null && nextNode.getKind() == SDGNode.Kind.CALL) {
                                possTgts = program.getSDG().getPossibleTargets(nextNode);
                                if (possTgts.isEmpty()) {
                                    String unresolvedCallTarget = nextNode.getUnresolvedCallTarget();
                                    if (unresolvedCallTarget != null && !call_methods.contains(unresolvedCallTarget)) {
                                        call_bw.write(unresolvedCallTarget +'\n');
                                        call_methods.add(unresolvedCallTarget);
                                    }
                                }
                                else {
                                    for (SDGNode entry : possTgts) {
                                        if (!call_methods.contains(entry.getBytecodeName())) {
                                            call_bw.write(entry.getBytecodeName()+'\n');
                                            call_methods.add(entry.getBytecodeName());
                                        }
                                    }
                                }
                                nextNode = program.getSDG().getNode(nextNode.getId()+1);
                                continue;
                            }
                            break;
                        }
                    }
                }
            }
            TObjectIntMap<IViolation<SDGProgramPart>> resultByProgramPart = ana.groupByPPPart(result);
            /** do something with result */
            for (IViolation<SDGProgramPart> vio : resultByProgramPart.keySet()) {
                System.out.println(vio);
            }

            /** search for method call inside method */
            List<SDGNode> call_nodes = new ArrayList<SDGNode>();
            List<SDGNode> rm_call_nodes = new ArrayList<SDGNode>();
            for (SDGNode n : program.getSDG().vertexSet()) {
                if (n.getKind() == SDGNode.Kind.CALL) {
                    call_nodes.add(n);
                }
            }
            boolean new_call_added = true;
            while (new_call_added) {
                new_call_added = false;
                for (SDGNode n : call_nodes) {
                    for (String call_method : call_methods) {
                        if (n.getBytecodeName().contains(call_method)) {
                            Collection<SDGNode> possTgts = program.getSDG().getPossibleTargets(n);
                            if (possTgts.isEmpty()) {
                                String unresolvedCallTarget = n.getUnresolvedCallTarget();
                                if (unresolvedCallTarget != null && !call_methods.contains(unresolvedCallTarget)) {
                                    call_bw.write(unresolvedCallTarget+'\n');
                                    call_methods.add(unresolvedCallTarget);
                                    new_call_added = true;
                                }
                            }
                            else {
                                for (SDGNode entry : possTgts) {
                                    if (!call_methods.contains(entry.getBytecodeName())) {
                                        call_bw.write(entry.getBytecodeName()+'\n');
                                        call_methods.add(entry.getBytecodeName());
                                        new_call_added = true;
                                    }
                                }
                            }
                            rm_call_nodes.add(n);
                            break;
                        }
                    }
                }
                for (SDGNode n : rm_call_nodes) {
                    call_nodes.remove(n);
                }
                rm_call_nodes.clear();
            }

            /** close write buffer */
            if_bw.close();
            if_prev_bw.close();
            call_bw.close();
            info_bw.close();

            for (String s : call_methods) {
                System.out.println("Method added: "+s);
            }
    }
}