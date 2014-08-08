package io.scrollback.ml;


/**
 *
 * @author kamal
 *
 */
public class NeuralNet {
    private double input[][];
    protected int sizeLayer[];//no of unit in i th layer
    protected double theta[][][];//i=layer j=unit k=value
    protected double alpha,lambda;

    protected int layers;
    private  int out[][];
    private int m;//input size
    private double D[][][];

    protected double lout[][];
    protected double act[][];

    protected double lastOut[][];
    /**
     * Make Neural network for training
     *
     * @param sizeLayer sizeLayer[i]=no of unit in i th layer.
     * @param input input matrix row vector define one input.
     * @param out output matrix.
     * default value of alpha=1,lambda=0
     */
    public NeuralNet(int sizeLayer[],double input[][],int out[][]){

        this.out=out;
        this.input=input;
        this.sizeLayer=sizeLayer;
        lambda=0;
        m=input.length;
        this.layers=sizeLayer.length;
        alpha=1;
    }

    /**
     * Make Neural network for calculate output.
     *
     * @param sizeLayer sizeLayer[i]=no of unit in i th layer.
     * default value of alpha=1,lambda=0
     */
    public NeuralNet(int sizeLayer[]){
        this.sizeLayer=sizeLayer;
        this.layers=sizeLayer.length;
        lambda=0;
        this.alpha=1;
    }


    /**
     *
     * @return cost J of network.
     */
    public double cost(){
        double J=0;
        for(int i=0;i<m;i++){
            double o[]=this.calculateOutput(input[i]);
            for(int j=0;j<sizeLayer[layers-1];j++){
                J+=(out[i][j]*Math.log(o[j]))-(1-out[i][j])*(Math.log(1-o[j]));
            }
        }
        J=J/m;

        double rg=0;
        if(lambda!=0){
            for(int i=0;i<theta.length;i++){
                for(int j=0;j<theta[i].length;j++){
                    for(int k=0;k<theta[i][j].length;k++){
                        rg+=Math.pow(theta[i][j][k], 2);
                    }
                }
            }
            rg=(lambda/(2*m))*rg;
            J=J+rg;
        }
        return J;
    }

    /**
     *
     * @param lastOut last output of network
     * @return cost J of network.
     */
    public double cost(double lastOut[][]){
        double J=0;
        for(int i=0;i<m;i++){
            double o[]=lastOut[i];
            for(int j=0;j<sizeLayer[layers-1];j++){
                J+=(out[i][j]*Math.log(o[j]))-(1-out[i][j])*(Math.log(1-o[j]));
            }
        }
        J=J/m;

        double rg=0;
        if(lambda!=0){
            for(int i=0;i<theta.length;i++){
                for(int j=0;j<theta[i].length;j++){
                    for(int k=0;k<theta[i][j].length;k++){
                        rg+=Math.pow(theta[i][j][k], 2);
                    }
                }
            }
            rg=(lambda/(2*m))*rg;
            J=J+rg;
        }
        return J;
    }





    public double[][][] getTheta(){
        return theta;
    }
    public void setOut(int o[][])
    {
        out=new int[o.length][o[0].length];

        for(int i=0;i<o.length;i++)
        {
            for(int j=0;j<o[i].length;j++)
            {
                out[i][j]=o[i][j];
            }
        }


    }

    /**
     * Random initialisation of theta.
     */


    public void initializeTheta(){
        theta=new double[layers-1][][];
        int t=0;
        for(int i=0;i<layers-1;i++){
            theta[i]=new double[sizeLayer[i+1]][sizeLayer[i]+t];
            for(int j=0;j<sizeLayer[i+1];j++){

                for(int k=0;k<sizeLayer[i]+t;k++){
                    theta[i][j][k]=1-2*Math.random();//random b/w -1 to 1
                    //System.out.println(theta[i][j][k]);
                }
            }
            t=1;
        }
    }

    public void setParameter(double alpha,double lambda){
        this.alpha=alpha;
        this.lambda=lambda;
    }



    /**
     * It sets  the variable act[][].
     * @param x input vector to neural network.
     * @return output vector of Neural network.
     */
    public double[] calculateOutput(double x[]){

        double in[] = null;
        double t[]=new double[x.length];
        act=new double[layers][];
        act[0]=new double[x.length];

        for(int i=0;i<act[0].length;i++){
            act[0][i]=x[i];

            t[i]=x[i];
        }

        for(int i=0;i<layers-1;i++)
        {
            int ab=(sizeLayer[i+1]+1);
            int b=1;
            if(i!=layers-2)
                in=new double[ab];
            else{

                b=0;
                ab--;
                in=new double[ab];
            }
            act[i+1]=new double[ab];
            if(i!=layers-2){
                act[i+1][0]=1;
                in[0]=1;
            }
            for(int j=0;j<ab-b;j++){

                in[j+b]=this.activation(t,theta[i][j]);

                act[i+1][j+b]=in[j+b];
            }

            t=in;

        }


        return t;
    }
    /**
     * It sets  the variable act[][].
     * @param x input vector to neural network.
     * @return output vector of Neural network.
     */
    public double[] calculateOutput(int x[]){

        double in[] = null;
        double t[]=new double[x.length];
        act=new double[layers][];
        act[0]=new double[x.length];

        for(int i=0;i<act[0].length;i++){
            act[0][i]=x[i];

            t[i]=x[i];
        }

        for(int i=0;i<layers-1;i++)
        {
            int ab=(sizeLayer[i+1]+1);
            int b=1;
            if(i!=layers-2)
                in=new double[ab];
            else{

                b=0;
                ab--;
                in=new double[ab];
            }
            act[i+1]=new double[ab];
            if(i!=layers-2){
                act[i+1][0]=1;
                in[0]=1;
            }
            for(int j=0;j<ab-b;j++){

                in[j+b]=this.activation(t,theta[i][j]);

                act[i+1][j+b]=in[j+b];
            }

            t=in;

        }


        return t;
    }



    public void setTheta(double t[][][]){
        this.theta=t;
    }

    /**
     x input vector t weight vector
     */
    private double sigma(double x1[],double t1[]){
        double sum=0;
		/*GVector gx=new GVector(x1);
		GVector gt=new GVector(t1);
		
		GMatrix g=new GMatrix(1,1);
		
		g.mul(gx, gt);
		System.out.println(""+g.getElement(0, 0)+","+g.getNumCol()+","+g.getNumRow());
		return g.getElement(0, 0);
		*/
        //System.out.println(x1.length);
        for(int i=0;i<x1.length;i++){
            sum+=(x1[i]*t1[i]);
        }
		/*	if(sum>100)
			System.out.println("sum="+sum);*/
        return sum;
    }
    private double sigmoid(double v){

        double ret=1.0/(1+Math.exp(-1*v));

        return ret;
    }
    /**
     * activation for given input vector and Theta.
     * @param x1 input vector.
     * @param t1 Theta.
     * @return activation value.
     */
    public double activation(double x1[],double t1[]){
        return sigmoid(sigma(x1,t1));
    }
    /**
     *
     * @param ir no of iteration
     * @return theta
     */
    public double[][][] learnNeuralNetwork(int ir)
    {	//int variable for function

        double cdelta[][][]=new double[theta.length][][];
        D=new double[theta.length][][];   //derivative

        for(int i=0;i<cdelta.length;i++){
            cdelta[i]=new double[theta[i].length][];
            D[i]=new double[theta[i].length][];
            for(int j=0;j<cdelta[i].length;j++){
                cdelta[i][j]=new double[theta[i][j].length];
                D[i][j]=new double[theta[i][j].length];
            }
        }



        for(int g=0;g<ir;g++){
            for(int l=0;l<m;l++){
                this.calculateOutput(input[l]);

                double delta[][]=new double[layers-1][];//layers to 1. not for 0 th layer [(layer-2) to 0]

                delta[layers-2]=new double[sizeLayer[layers-1]];

                for(int i=0;i<delta[layers-2].length;i++)
                {
                    delta[layers-2][i]=act[layers-1][i]-out[l][i];
                }


                for(int i=layers-3;i>=0;i--)
                {
                    delta[i]=new double[sizeLayer[i+1]];

                    for(int j=0;j<delta[i].length;j++){
                        for(int k=0;k<sizeLayer[i+2];k++){

                            delta[i][j]=delta[i][j]+(theta[i+1][k][j+1]*delta[i+1][k]);
                        }
                        delta[i][j]=delta[i][j]*act[i+1][j+1]*(1.0-act[i+1][j+1]);//indexing of act and delta defer by 1  //first value of act is bias.
                    }
                }

                //end of calculation of delta

                for(int i=0;i<cdelta.length;i++){

                    for(int j=0;j<cdelta[i].length;j++){

                        for(int k=0;k<cdelta[i][j].length;k++){

                            cdelta[i][j][k]=cdelta[i][j][k]+act[i][k]*delta[i][j];//delta i correspond to next layer (0-layers-1)

                        }
                    }
                }


            }//end of loop l

            for(int i=0;i<cdelta.length;i++){
                for(int k=0;k<cdelta[i][0].length;k++){
                    D[i][0][k]=(1.0/m)*cdelta[i][0][k];//j=0

                }

            }
            for(int i=0;i<cdelta.length;i++){

                for(int j=1;j<cdelta[i].length;j++){

                    for(int k=0;k<cdelta[i][j].length;k++){
                        //System.out.println(cdelta[i][j][k]);
                        D[i][j][k]=(1.0/m)*cdelta[i][j][k]+lambda*theta[i][j][k];//j!=0
                        //System.out.println(cdelta[i][j][k]+","+	D[i][j][k]);
                    }
                }
            }



            for(int i=0;i<theta.length;i++)
            {

                for(int j=0;j<theta[i].length;j++)
                {

                    for(int k=0;k<theta[i][j].length;k++)
                    {
                        theta[i][j][k]=theta[i][j][k]-alpha*D[i][j][k];
                    }
                }
            }


        }//end of lr loop
        return theta;
    }

    /**
     *
     * single iteration.
     * @return cost before updation of theta.
     */
    public double learnNeuralNetwork()
    {	//int variable for function

        double cdelta[][][]=new double[theta.length][][];
        D=new double[theta.length][][];   //derivative

        for(int i=0;i<cdelta.length;i++){
            cdelta[i]=new double[theta[i].length][];
            D[i]=new double[theta[i].length][];
            for(int j=0;j<cdelta[i].length;j++){
                cdelta[i][j]=new double[theta[i][j].length];
                D[i][j]=new double[theta[i][j].length];
            }
        }


        lastOut=new double[m][];

        for(int l=0;l<m;l++){
            lastOut[l]=this.calculateOutput(input[l]);

            double delta[][]=new double[layers-1][];//layers to 1. not for 0 th layer [(layer-2) to 0]

            delta[layers-2]=new double[sizeLayer[layers-1]];

            for(int i=0;i<delta[layers-2].length;i++)
            {
                delta[layers-2][i]=act[layers-1][i]-out[l][i];
            }


            for(int i=layers-3;i>=0;i--)
            {
                delta[i]=new double[sizeLayer[i+1]];

                for(int j=0;j<delta[i].length;j++){
                    for(int k=0;k<sizeLayer[i+2];k++){

                        delta[i][j]=delta[i][j]+(theta[i+1][k][j+1]*delta[i+1][k]);
                    }
                    delta[i][j]=delta[i][j]*act[i+1][j+1]*(1.0-act[i+1][j+1]);//indexing of act and delta defer by 1  //first value of act is bias.
                }
            }

            //end of calculation of delta

            for(int i=0;i<cdelta.length;i++){

                for(int j=0;j<cdelta[i].length;j++){

                    for(int k=0;k<cdelta[i][j].length;k++){

                        cdelta[i][j][k]=cdelta[i][j][k]+act[i][k]*delta[i][j];//delta i correspond to next layer (0-layers-1)

                    }
                }
            }


        }//end of loop l

        for(int i=0;i<cdelta.length;i++){
            for(int k=0;k<cdelta[i][0].length;k++){
                D[i][0][k]=(1.0/m)*cdelta[i][0][k];//j=0

            }

        }
        for(int i=0;i<cdelta.length;i++){

            for(int j=1;j<cdelta[i].length;j++){

                for(int k=0;k<cdelta[i][j].length;k++){
                    //System.out.println(cdelta[i][j][k]);
                    D[i][j][k]=(1.0/m)*cdelta[i][j][k]+lambda*theta[i][j][k];//j!=0
                    //System.out.println(cdelta[i][j][k]+","+	D[i][j][k]);
                }
            }
        }



        for(int i=0;i<theta.length;i++)
        {

            for(int j=0;j<theta[i].length;j++)
            {

                for(int k=0;k<theta[i][j].length;k++)
                {
                    theta[i][j][k]=theta[i][j][k]-alpha*D[i][j][k];
                }
            }
        }



        return this.cost(lastOut);
    }
    /**
     *
     * @return regularization parameter of network.
     */
    public double getLambda() {

        return lambda;
    }
    /**
     *
     * @return return learning rate of Network.
     */
    public double getAlpha() {

        return alpha;
    }

    /**
     *  shuffle the data at random.
     * @param input input
     * @param out out
     * @param sufpara no of time loop will shuffle the values
     */
    public void shuffle(double input[][], int out[][],int sufpara) {
        for (int i = 0; i < sufpara; i++) {
            int r1 = (int) (Math.random() * (input.length+1));
            int r2 = (int) (Math.random() * (input.length+1));
            // System.out.println(r1+","+r2);
            double d[] = new double[input[r1].length];
            for (int j = 0; j < input[r1].length; j++) {
                d[j] = input[r1][j];
            }
            for (int j = 0; j < input[r2].length; j++) {
                input[r1][j] = input[r2][j];
            }
            for (int j = 0; j < input[r2].length; j++) {
                input[r2][j] = d[j];
            }
            int dt[] = new int[out[r1].length];
            for (int j = 0; j < out[r1].length; j++) {
                dt[j] = out[r1][j];
            }
            for (int j = 0; j < out[r2].length; j++) {
                out[r1][j] = out[r2][j];
            }
            for (int j = 0; j < out[r2].length; j++) {
                out[r2][j] = dt[j];
            }

        }

    }




}

