import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.*;

public class HCODA {
    HashMap<String,Integer> zNode;
    HashMap<String,Integer> zEdgeNode;
    double[][] betaNode;
    double[][] betaEdgeNode;
    int rNodeNum;
    int rEdgeNodeNum;
    int cNum;
    int aNodeNum;
    int aEdgeNodeNum;
    int iterNum;
    double lambda1;
    double lambda2;
    double lambda3;
    double a0;
    String direc;
    String dataNode;
    String dataEdgeNode;
    String link_node_node;
    String link_node_edge;
    String results;

    public HCODA(int recordNumNode,int recordNumEdgeNode, int clusterNum, int attributeNumNode, int attributeNumEdgeNode) {
        this.rNodeNum = recordNumNode;
        this.rEdgeNodeNum = recordNumEdgeNode;
        this.cNum = clusterNum;
        this.aNodeNum = attributeNumNode;
        this.aEdgeNodeNum = attributeNumEdgeNode;
        this.iterNum = 10;
        this.betaNode = new double[clusterNum][attributeNumNode];
        this.betaEdgeNode = new double[clusterNum][attributeNumEdgeNode];
        zNode = new HashMap<String,Integer>(recordNumNode);
        zEdgeNode = new HashMap<String,Integer>(recordNumEdgeNode);
    }

    public void initializationNode(String initialzNode) {
        System.out.println("Initializing Node Labels");
        String filename = String.valueOf(this.direc) + initialzNode;
        FileOperate.readz((String)filename, (HashMap<String,Integer>)this.zNode);
    }
    public void initializationEdgeNode(String initialzEdgeNode) {
        System.out.println("Initializing Edge-Node Labels");
        String filename = String.valueOf(this.direc) + initialzEdgeNode;
        FileOperate.readz((String)filename, (HashMap<String,Integer>)this.zEdgeNode);
    }

    public void MstepBetaNode() {
        int j;
        int k;
        FileInputStream in = FileOperate.readdata((String)(String.valueOf(this.direc) + this.dataNode));
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String[] attLine = new String[2 * this.aNodeNum];
        double[][] words = new double[this.cNum][this.aNodeNum];
        double[] components = new double[this.cNum];
        double alpha = 1.0E-4;
        for (j = 0; j < this.cNum; ++j) {
            for (k = 0; k < this.aNodeNum; ++k) {
                words[j][k] = 0.0;
            }
            components[j] = 0.0;
        }
        
        try {
            if (br != null) {
                String strLine;
                while ((strLine = br.readLine()) != null) {
                    attLine = strLine.split("\\|");
                    //j stores community number of ith node
                    j = this.zNode.get(attLine[0]) - 1;
                    int curInd=0;
                    for (k = 1; k < attLine.length; k+=1) {
                        double curData = Double.parseDouble(attLine[k]);
                        
                        if (j > -1) {
                        	words[j][curInd] += curData; 
                        	components[j]+=curData;
                        }
                        curInd++;
                    }
                }
                for (j = 0; j < this.cNum; ++j) {
                    for (k = 0; k < this.aNodeNum; ++k) {
                    	this.betaNode[j][k] = words[j][k] / components[j] ;
                    }
                }
            }
        }
        catch (Exception e) {
        	e.printStackTrace();
        }
        FileOperate.closeFile((FileInputStream)in);
    }

	public void MstepBetaEdgeNode() {
        int j;
        int k;
        FileInputStream in = FileOperate.readdata((String)(String.valueOf(this.direc) + this.dataEdgeNode));
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String[] attLine = new String[2 * this.aEdgeNodeNum];
        double[][] words = new double[this.cNum][this.aEdgeNodeNum];
        double[] components = new double[this.cNum];
        double alpha = 1.0E-4;
        for (j = 0; j < this.cNum; ++j) {
            for (k = 0; k < this.aEdgeNodeNum; ++k) {
                words[j][k] = 0.0;
            }
            components[j] = 0.0;
        }
        
        try {
            if (br != null) {
                String strLine;
                while ((strLine = br.readLine()) != null) {
                    attLine = strLine.split("\\|");
                    
                    String[] auth1 = attLine[0].split("_");
                    j = -9999;
                    if(this.zEdgeNode.containsKey("edge_"+auth1[1]+"_"+auth1[2]))
                    {
                    	attLine[0] = "edge_"+auth1[1]+"_"+auth1[2];
                    	//check if this kth neighbor has community value 0 i.e. outlier
                    	if (this.zEdgeNode.get(attLine[0]) == 0) continue;
                    
                    	j = this.zEdgeNode.get(attLine[0]) - 1;
                    }
                    else if(this.zEdgeNode.containsKey("edge_"+auth1[2]+"_"+auth1[1]))
                    {
                    	attLine[0] = "edge_"+auth1[2]+"_"+auth1[1];
                    	//check if this kth neighbor has community value 0 i.e. outlier
                    	if (this.zEdgeNode.get(attLine[0]) == 0) continue;
                    
                    	j = this.zEdgeNode.get(attLine[0]) - 1;
                    }
                    
                    int curInd = 0;
                    for (k = 1; k < attLine.length; k+=1) {
                        double curData = Double.parseDouble(attLine[k]);
                        if (j > -1) {
                        	words[j][curInd] += curData; 
                        	components[j]+=curData;
                        }
                        curInd++;
                    }
                }
                for (j = 0; j < this.cNum; ++j) {
                    for (k = 0; k < this.aEdgeNodeNum; ++k) {
                    	this.betaEdgeNode[j][k] = words[j][k] / components[j] ;
                    }
                }
            }
        }
        catch (Exception e) {
        	e.printStackTrace();
        }
        FileOperate.closeFile((FileInputStream)in);
    }
	
	
    public void EstepKLNode() {
        FileInputStream in = FileOperate.readdata((String)(String.valueOf(this.direc) + this.dataNode));
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        FileInputStream inNN = FileOperate.readdata((String)(String.valueOf(this.direc) + this.link_node_node));
        BufferedReader brNN = new BufferedReader(new InputStreamReader(inNN));
        FileInputStream inNE = FileOperate.readdata((String)(String.valueOf(this.direc) + this.link_node_edge));
        BufferedReader brNE = new BufferedReader(new InputStreamReader(inNE));
        String[] attLine = new String[this.aNodeNum];
        String[] neighborNode = new String[this.rNodeNum];
        String[] neighborEdgeNode = new String[this.rEdgeNodeNum];
        double[] curData = new double[this.aNodeNum];
        double[] Unormal = new double[this.cNum];
        double[] neighborEffect = new double[this.cNum];
        double[] neighborEffectEdgeNode = new double[this.cNum];
        double[] neighborEffectTriangle = new double[this.cNum];
        IndexArray[] IndexMinU = new IndexArray[this.rNodeNum];
        int[] curInd = new int[this.aNodeNum];
        long numOutlier = Math.round(this.a0 * (double)this.rNodeNum);
        int i = 0; //start with 1st node
        try {
            if (br != null && brNN != null) {
                String strLine;
                String nnLine;
                String neLine;
                while ((strLine = br.readLine()) != null && (nnLine = brNN.readLine()) != null && (neLine = brNE.readLine()) != null) {
                    int k;
                    int j;
                    attLine = strLine.split("\\|");
                    String currNode = attLine[0];
                    neighborNode = nnLine.split("\\|");
                    neighborEdgeNode = neLine.split("\\|");
                    double neighborSum = 0.0;
                    double neighborSumEdgeNode = 0.0;
                    double neighborSumTriangle = 0.0;
                    
                    for (j = 0; j < this.cNum; ++j) {
                        Unormal[j] = 0.0;
                        neighborEffect[j] = 0.0;
                        neighborEffectEdgeNode[j] = 0.0;
                        neighborEffectTriangle[j] = 0.0;
                    }
                    
                    /**
                      **node::node calculation**
                      **/
                    for (k = 1; k < neighborNode.length; k+=2) {
                    	//get the kth neighbor
                        String nInd = neighborNode[k];
                        
                        //check if this kth neighbor has community value 0 i.e. outlier
                        if (this.zNode.get(nInd) == 0) continue;
                        
                        
                        int n = this.zNode.get(nInd) - 1; 
                        neighborEffect[n] = neighborEffect[n] + Double.parseDouble(neighborNode[k + 1]);
                        
                        neighborSum+=Double.parseDouble(neighborNode[k + 1]);
                        
                    }
                    for (j = 0; j < this.cNum; ++j) {
                        if (neighborSum == 0.0) continue;
                        neighborEffect[j] = neighborEffect[j] / neighborSum;
                    }
                    
                    /***
                      **node::edge_node calculation**
                      **/
                    for (k = 1; k < neighborEdgeNode.length; k+=2) {
                    	//get the kth neighbor
                        String nInd = neighborEdgeNode[k];
                        String[] auth1 = nInd.split("_");
                        int n = -9999;
                        if(this.zEdgeNode.containsKey("edge_"+auth1[1]+"_"+auth1[2]))
                        {
                        	nInd = "edge_"+auth1[1]+"_"+auth1[2];
                        	//check if this kth neighbor has community value 0 i.e. outlier
                        	if (this.zEdgeNode.get(nInd) == 0) continue;
                        
                        	n = this.zEdgeNode.get(nInd) - 1;
                        }
                        else if(this.zEdgeNode.containsKey("edge_"+auth1[2]+"_"+auth1[1]))
                        {
                        	nInd = "edge_"+auth1[2]+"_"+auth1[1];
                        	//check if this kth neighbor has community value 0 i.e. outlier
                        	if (this.zEdgeNode.get(nInd) == 0) continue;
                        
                        	n = this.zEdgeNode.get(nInd) - 1;
                        }
                        neighborEffectEdgeNode[n] = neighborEffectEdgeNode[n] + Double.parseDouble(neighborEdgeNode[k + 1]);
                        
                        neighborSumEdgeNode+=Double.parseDouble(neighborEdgeNode[k + 1]);
                    }
                    for (j = 0; j < this.cNum; ++j) {
                        if (neighborSumEdgeNode == 0.0) continue;
                        neighborEffectEdgeNode[j] = neighborEffectEdgeNode[j] / neighborSumEdgeNode;
                    }
                    
                    /**
                      **Triangle clique**
                      **/
                    for (k = 1; k < neighborNode.length; k+=2) {
                    //calculate community and then assign
                    	String nInd = neighborNode[k];
                    	int vi = this.zNode.get(currNode);
                        int vj = this.zNode.get(nInd);
                        int eij = -999;
                        int n = -999;
                        String edge = "edge_"+currNode+"_"+nInd;
                        if(zEdgeNode.containsKey(edge))
                        {
                        	eij = zEdgeNode.get(edge);
                        }
                        else
                        {
                        	edge = "edge_"+nInd+"_"+currNode;
	                    	if(zEdgeNode.containsKey(edge))
                        		eij = zEdgeNode.get(edge);
                        }
                        if(vi==vj && vj==eij && vi==eij && vi!=0)
                        {
                        	n = vi-1;
                        }
                        else
                        	continue;
                        
                        neighborEffectTriangle[n] = neighborEffectTriangle[n] + Double.parseDouble(neighborNode[k + 1])+1+1;
                        neighborSumTriangle+=Double.parseDouble(neighborNode[k + 1])+1+1;
                    }
                    for (j = 0; j < this.cNum; ++j) {
                    	if (neighborSumTriangle == 0.0) continue;
                    	neighborEffectTriangle[j] = neighborEffectTriangle[j] / neighborSumTriangle;
                    }
                    
                    //conditional probability calculation
                    double docLen = 0.0;
                    for (k = 1; k < attLine.length; k+=1) {
                        curInd[k-1] = k-1;
                        curData[k-1] = Double.parseDouble(attLine[k]);
                        docLen+=curData[k-1];
                    }
                    for (j = 0; j < this.cNum; ++j) {
                        for (k = 0; k < attLine.length-1; ++k) {
                            /** calculates the likelihood of data i.e P(x=s|z=k). As it follows multinomial distribution 
                             * it calculates likelihood according to equation 4 in paper
                            **/
                            Unormal[j] = Unormal[j]+curData[k] * Math.log(this.betaNode[j][curInd[k]]) / docLen;
                           
                        }
                    }
                    IndexMinU[i] = new IndexArray();
                    IndexMinU[i].index = currNode;//i;
                    IndexMinU[i].value = -1.0E9;
                    for (j = 0; j < this.cNum; ++j) {
                    	/**
                    	 *  calculates the posterior probability
                    	 * */
                    	double Uall = ((this.lambda1)*neighborEffect[j])+((this.lambda2)*neighborEffectEdgeNode[j])+((this.lambda3)*neighborEffectTriangle[j]) + Unormal[j];
                        //calculates U_i(k) of eq 6 in paper
                        if (Uall <= IndexMinU[i].value) continue;
                        IndexMinU[i].value = Uall;
                        //select the min U_i among all cluster for current node and assigns that community value to current node.
                        this.zNode.put(currNode,j + 1);
                    }
                    ++i;
                }
                
                /**after all nodes are assigned new community labels,
                 * sort the U_i in ascending order and make the least scored a0% of U_i values as outlier
                 * */
                Arrays.sort(IndexMinU);
                i = 0;
                while ((long)i < numOutlier) {
                    String index = IndexMinU[i].index;
                    this.zNode.put(index, 0);
                    ++i;
                }
            }
        }
        catch (Exception e) {
        	e.printStackTrace();
        }
        FileOperate.closeFile((FileInputStream)in);
    }
	
	public void EstepKLEdgeNode() {
        FileInputStream in = FileOperate.readdata((String)(String.valueOf(this.direc) + this.dataEdgeNode));
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String[] attLine = new String[this.aEdgeNodeNum];
        String[] neighborNode = new String[2];// for eij, only neighbors are vi and vj
        double[] curData = new double[this.aEdgeNodeNum];
        double[] Unormal = new double[this.cNum];
        double[] neighborEffect = new double[this.cNum];
        IndexArray[] IndexMinU = new IndexArray[this.rEdgeNodeNum];
        int[] curInd = new int[this.aEdgeNodeNum];
        long numOutlier = Math.round(this.a0 * (double)this.rEdgeNodeNum);
        int i = 0;
        try {
            if (br != null) {
                String strLine;
                while ((strLine = br.readLine()) != null) {
                    int k;
                    int j;
                    attLine = strLine.split("\\|");
                    String currNode = attLine[0];
                    
                    double neighborSum = 0.0;
                    
                    for (j = 0; j < this.cNum; ++j) {
                        Unormal[j] = 0.0;
                        neighborEffect[j] = 0.0;
                    }
                    
                    /**
                      ** edge_node::node calculation**
                      **/
                    String[] nodes = currNode.split("_");
                    for (k = 0; k < neighborNode.length; k++) {
                        
                        String nInd = nodes[k+1];
                        
                        //check if this kth neighbor has community value 0 i.e. outlier
                        if (this.zNode.get(nInd) == 0) continue;
                        
                        int n = this.zNode.get(nInd) - 1; 
                        neighborEffect[n] = neighborEffect[n] + 1;
                        neighborSum += 1;
                        
                    }
                    for (j = 0; j < this.cNum; ++j) {
                        if (neighborSum == 0.0) continue;
                        neighborEffect[j] = neighborEffect[j] / neighborSum;
                    }
                    
                    //conditional probability calculation
                    double docLen = 0.0;
                    for (k = 1; k < attLine.length; k++) {
                        curInd[k-1] = k-1;
                        curData[k-1] = Double.parseDouble(attLine[k]);
                        docLen+=curData[k-1];
                    }
                    for (j = 0; j < this.cNum; ++j) {
                        for (k = 0; k < attLine.length-1; ++k) {
                            Unormal[j] = Unormal[j]+curData[k] * Math.log(this.betaEdgeNode[j][curInd[k]]) / docLen;
                        }
                    }
                    IndexMinU[i] = new IndexArray();
                    IndexMinU[i].index = currNode;//i;
                    IndexMinU[i].value = -1.0E9;
                    for (j = 0; j < this.cNum; ++j) {
                        double Uall = neighborEffect[j] + Unormal[j];
                        //calculates U_i(k) of eq 6 in paper
                        if (Uall <= IndexMinU[i].value) continue;
                        IndexMinU[i].value = Uall;
                        //select the min U_i among all cluster for current node and assigns that community value to current node.
                        this.zEdgeNode.put(currNode,j + 1);
                    }
                    ++i;
                }
                
                /**after all nodes are assigned new community labels,
                 * sort the U_i in ascending order and make the least scored a0% of U_i values as outlier
                 * */
                Arrays.sort(IndexMinU);
                i = 0;
                while ((long)i < numOutlier) {
                    String index = IndexMinU[i].index;
                    this.zEdgeNode.put(index, 0);
                    ++i;
                }
            }
        }
        catch (Exception e) {
        	e.printStackTrace();
        }
        FileOperate.closeFile((FileInputStream)in);
    }

	
    public void iterationKL() {
        System.out.println("Executing HCODA...");
        HashMap<String, Integer> zNodeold = new HashMap<String, Integer>(this.rNodeNum);
        HashMap<String, Integer> zEdgeNodeold = new HashMap<String, Integer>(this.rEdgeNodeNum);
        int zitnum = 5;
        double zdifTh = 0.01;
        int i = 0;
        block0 : for (i = 0; i < this.iterNum; ++i) {
            this.MstepBetaNode();
            this.MstepBetaEdgeNode();
            
            double zdif = 1.0;
            for (int cit = 0; cit < zitnum; ++cit) {
                this.EstepKLNode();
                this.EstepKLEdgeNode();
                
                zdif = 0.0;
                for (String author:this.zNode.keySet()) {
                    if (zNodeold.get(author) == this.zNode.get(author)) continue;
                    zdif+=1.0;
                }
                zNodeold.putAll(this.zNode);
                if ((zdif/=(double)this.rNodeNum) <= zdifTh) continue block0;

                //edge node
                zdif = 0.0;
                for (String author:this.zEdgeNode.keySet()) {
                    if (zEdgeNodeold.get(author) == this.zEdgeNode.get(author)) continue;
                    zdif+=1.0;
                }
                zEdgeNodeold.putAll(this.zEdgeNode);
                if ((zdif/=(double)this.rEdgeNodeNum) <= zdifTh) continue block0;
                
            }
        }
    }

    public void outputResult() {
        try {
            FileOutputStream out = new FileOutputStream(String.valueOf(this.direc) + this.results);
            PrintStream p = new PrintStream(out);
            for (String author:this.zNode.keySet()) {
            	if (this.zNode.get(author) != 0) continue;
            	p.println(author);
            }
            p.println("*************Edge nodes*************\n");
            for (String author:this.zEdgeNode.keySet()) {
                if (this.zEdgeNode.get(author) != 0) continue;
                p.println(author);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    

    public static void main(String[] args) {
        int recordNodeNum = Integer.parseInt(args[0]);
        int recordEdgeNodeNum = Integer.parseInt(args[1]);
        int clusterNum = Integer.parseInt(args[2]);
        int attributeNodeNum = Integer.parseInt(args[3]);
        int attributeEdgeNodeNum = Integer.parseInt(args[4]);
        HCODA od1 = new HCODA(recordNodeNum, recordEdgeNodeNum, clusterNum, attributeNodeNum, attributeEdgeNodeNum);
        od1.lambda1 = Double.parseDouble(args[5]);
        od1.lambda2 = Double.parseDouble(args[6]);
        od1.lambda3 = Double.parseDouble(args[7]);
        od1.a0 = Double.parseDouble(args[8]);
        od1.direc = args[9];
        od1.initializationNode(args[10]);
        od1.initializationEdgeNode(args[11]);
        od1.dataNode = args[12];
        od1.dataEdgeNode = args[13];
        od1.link_node_node = args[14];
        od1.link_node_edge = args[15];
        od1.results = args[16];
        od1.iterationKL();
        od1.outputResult();
        System.out.println("Result generated in file: "+args[16]);
    }
}
