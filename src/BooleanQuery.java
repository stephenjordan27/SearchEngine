
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.TreeMap;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author asus
 */
public class BooleanQuery {
    private static TreeMap<String,ArrayList<String>> dictionary;
    private String query;
    
    public BooleanQuery(TreeMap<String,ArrayList<String>> dictionary,String query) {
        this.dictionary = dictionary;
        this.query = query;
    }
    
    public ArrayList<String> documentBooleanQuery(){
        List<Integer> vector = this.ProcessBooleanQuery();
        ArrayList<String> result = new ArrayList<String>();
        for (int i = 0; i < vector.size(); i++) {
            if(vector.get(i)==1){
                if(i<=9){
                    result.add("Doc00"+(i+1)+".txt");
                }
                else if(i<=99){
                    result.add("Doc0"+(i+1)+".txt");
                }
                else{
                    result.add("Doc"+(i+1)+".txt");
                }
                
            }
        }
        return result;
        
    }
    
    public List<Integer> ProcessBooleanQuery() {
        String[] queryTerm = query.toLowerCase().split(" ");
        List<Integer> operands = null;
        String operator = "";
        boolean hasNotOperator = false;
        
        for(String term : queryTerm){
            if(operator == "" ||  operands == null && !term.equalsIgnoreCase("NOT")){
                if(term.equalsIgnoreCase("AND") || term.equalsIgnoreCase("OR")){
                    operator = term;
                }
                else{
                    operands = GetTermIncidenceVector(term);
                    
                }
            }
            else if(term.equalsIgnoreCase("NOT")){
                hasNotOperator = true;
            }
            else if(hasNotOperator && operator != ""){
                operands = ProcessBooleanOperator(operator,operands,ProcessBooleanOperator("NOT",GetTermIncidenceVector(term),null));
                hasNotOperator = false;
            }
            else if(hasNotOperator && operator == ""){
                operands = ProcessBooleanOperator("NOT",operands,null);
            }
            else{
                operands = ProcessBooleanOperator(operator,operands,GetTermIncidenceVector(term));
                operator = "";
            }
            
        }
        return operands;
    }
    
    
    public List<Integer> GetTermIncidenceVector(String term)
    {
        List<String> documents = dictionary.get(term);
        List<Integer> incidenceVector = new ArrayList<Integer>();
        int docindex = 0;
        for (int i = 1; i <= 154; i++) {
            //incidence vector for each terms
            try{
                if (documents != null && docindex < documents.size() && Integer.parseInt(documents.get(docindex).substring(3,6)) == i){
                    //document contains the term
                    incidenceVector.add(1); 
                    docindex++;
                }
                else{
                    //document do not contains the term
                    incidenceVector.add(0); 
                }
            }
            catch(Exception e){
                System.out.println("");
            }
            
        }
        return incidenceVector;
    } 
    
    
    public List<Integer> ProcessBooleanOperator(String op, 
          List<Integer> previousTermV,List<Integer> nextTermV)
    {
        List<Integer> resultSet = new ArrayList<Integer>();
        if(op.equalsIgnoreCase("NOT"))
        {
            for(Integer a : previousTermV)
            {
                if (a == 1)
                {
                    resultSet.add(0);
                }
                else
                {
                    resultSet.add(1);
                }
            }
        }
        else if (op.equalsIgnoreCase("AND")) //bitwise AND operation
        {
            for (Integer a = 0; a < previousTermV.size(); a++)
            {
                if (previousTermV.get(a) == 1 && nextTermV.get(a) == 1)
                {
                    resultSet.add(1);
                }
                else
                {
                    resultSet.add(0);
                }
            }
        }
        else if (op.equalsIgnoreCase("OR")) //bitwise OR operation
        {
            for (Integer a = 0; a < previousTermV.size(); a++)
            {
                if (previousTermV.get(a) == 0 && nextTermV.get(a) == 0)
                {
                    resultSet.add(0);
                }
                else
                {
                    resultSet.add(1);
                }
            }
        }
        return resultSet;
    }

}
