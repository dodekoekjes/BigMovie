package com.groep2.Chatbot;

import org.rosuda.REngine.*;

public class RHandler {
    String javaVector;
//    REngine re = new REngine() {
//        @Override
//        public REXP parse(String text, boolean resolve) throws REngineException {
//            return null;
//        }
//
//        @Override
//        public REXP eval(REXP what, REXP where, boolean resolve) throws REngineException, REXPMismatchException {
//            return null;
//        }
//
//        @Override
//        public void assign(String symbol, REXP value, REXP env) throws REngineException, REXPMismatchException {
//
//        }
//
//        @Override
//        public REXP get(String symbol, REXP env, boolean resolve) throws REngineException, REXPMismatchException {
//            return null;
//        }
//
//        @Override
//        public REXP resolveReference(REXP ref) throws REngineException, REXPMismatchException {
//            return null;
//        }
//
//        @Override
//        public REXP createReference(REXP value) throws REngineException, REXPMismatchException {
//            return null;
//        }
//
//        @Override
//        public void finalizeReference(REXP ref) throws REngineException, REXPMismatchException {
//
//        }
//
//        @Override
//        public REXP getParentEnvironment(REXP env, boolean resolve) throws REngineException, REXPMismatchException {
//            return null;
//        }
//
//        @Override
//        public REXP newEnvironment(REXP parent, boolean resolve) throws REngineException, REXPMismatchException {
//            return null;
//        }
//    };

    public RHandler(String javaVector) throws REXPMismatchException, REngineException {
        // initialize the vector
        this.javaVector = javaVector;

//        // The vector that was created in JAVA context is stored in 'rVector' which is a variable in R context.


//
//        //Calculate MEAN of vector using R syntax.
//        re.eval("meanVal=mean(rVector)");
//
//        //Retrieve MEAN value
//        double mean = re.eval("meanVal").asDouble();
//
//        //Print output values
//        System.out.println("Mean of given vector is=" + mean);

    }

    public void Execute(){

    }
}
