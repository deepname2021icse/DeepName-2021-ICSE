import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import soot.G;
import soot.PackManager;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;

import soot.options.Options;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import java.io.*;


public class graphRun {
	public static void main(String[] args) {
		G.reset();
		Options.v().set_process_dir(Arrays.asList());//Put data path
		//Options.v().set_process_dir(Collections.singletonList());
		Options.v().keep_line_number();
		Options.v().set_src_prec(Options.src_prec_java);
		Options.v().set_allow_phantom_refs(true);
		Options.v().set_prepend_classpath(true);
		Options.v().set_output_format(Options.output_format_jimple);
		Options.v().set_no_bodies_for_excluded(true);
		Options.v().set_whole_program(true);
		Options.v().set_verbose(true);
		Options.v().setPhaseOption("cg.spark", "on");
		Scene.v().loadBasicClasses();
		Scene.v().loadNecessaryClasses();
		List<SootMethod> entryPoints = new ArrayList<SootMethod>();
		for (SootClass sc : Scene.v().getClasses()){
		    if (sc.declaresMethodByName("main")) {
		    	entryPoints.add(sc.getMethodByName("main"));
		    	System.out.println(sc);
		    }	    
		}
		Scene.v().setEntryPoints(entryPoints);
		PackManager.v().runPacks();
		CallGraph callgraph = Scene.v().getCallGraph();
        System.out.println("[TestSpark] Call graph size " + callgraph.size());
        FileWriter out_file = new FileWriter ("call_graph.txt", true);
        for (Edge edge : callgraph) {
        	out_file.write(edge.toString());
            System.out.println(edge + "");
        }
        out_file.close();
	}
}
