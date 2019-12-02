
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author asus
 */
public class PrecisionRecall {
        private ArrayList<String> displayResult,goldenAnswer;
        private int tp,fp,fn,tn,sumOfDocument;

	String name;

	public PrecisionRecall(int sumOfDocument,ArrayList<String> displayResult, ArrayList<String> goldenAnswer) {
            this.sumOfDocument = sumOfDocument;
            this.displayResult = displayResult;
            this.goldenAnswer = goldenAnswer;
            this.tp = 0;
            this.fp = 0;
            this.fn = 0;
            this.tn = 0;
	}
        
        public void generatePrecisionRecallVariable(){
            for(String documentNameDisplayResult:displayResult){
                for(String documentNameGoldenAnswer:goldenAnswer){
                    if(documentNameDisplayResult.equalsIgnoreCase(documentNameGoldenAnswer)){
                        this.tp++;
                        break;
                    }
                }
            }
            
            for(String documentNameDisplayResult:displayResult){
                boolean isDifferent = false;
                for(String documentNameGoldenAnswer:goldenAnswer){
                    if(documentNameDisplayResult.equalsIgnoreCase(documentNameGoldenAnswer)){
                        isDifferent = false;
                        break;
                    }
                    else{
                        isDifferent = true;
                    }
                }
                if(isDifferent)this.fp++;
            }
            
            for(String documentNameGoldenAnswer:goldenAnswer){
                boolean isDifferent = false;
                for(String documentNameDisplayResult:displayResult){
                    if(documentNameDisplayResult.equalsIgnoreCase(documentNameGoldenAnswer)){
                        isDifferent = false;
                        break;
                    }
                    else{
                        isDifferent = true;
                    }
                }
                if(isDifferent)this.fn++;
            }
            
            this.tn = this.sumOfDocument-this.tp-this.fp-this.fn;
            
            
        }

	public double precision() {															//
            return tp / (tp + fp);
	}  
        
        public double recall() {															//
            return tp / (tp + fn);
	}  

}