
/*
 * This file is the implement the Digital Signature Standard, Secret DSA key with ASN1.
 * Author:  Shuhang Zhou, Haoyan Li.
 * Course:  CSE 5673-E1 Cryptology, Fall 2013.
 * Project: Project 1.
 * Date:    NOV 10, 2013.
 * java DSS -p 1024 -q 160 -S d:\sk.txt -P d:\pk.txt
 * java DSS -M d:\message.txt -S d:\sk.txt -s d:\sign.txt
 * java DSS -M d:\message.txt -P d:\pk.txt -s d:\sign.txt
 */

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import ASN.ASN1DecoderFail;
import ASN.ASNObj;
import ASN.Decoder;
import ASN.Encoder;
class PK {
	BigInteger p;
	BigInteger q;
	BigInteger g;
	BigInteger y;
	public PK(){
		this.p=new BigInteger("0");
		this.q=new BigInteger("0");
		this.g=new BigInteger("0");
		this.y=new BigInteger("0");
	}
	public void setPK(BigInteger p,BigInteger q,BigInteger g,BigInteger y)
	{
		this.p=p;
		this.q=q;
		this.g=g;
		this.y=y;
	}
}

class SK {
	BigInteger p;
	BigInteger q;
	BigInteger g;
	BigInteger x;
	public SK(){
		this.p=new BigInteger("0");
		this.q=new BigInteger("0");
		this.g=new BigInteger("0");
		this.x=new BigInteger("0");
	}
	public void setSK(BigInteger p,BigInteger q,BigInteger g,BigInteger x)
	{
		this.p=p;
		this.q=q;
		this.g=g;
		this.x=x;
	}
}

class DSASignature {
	BigInteger r;
	BigInteger s;
	public DSASignature(){
		this.r=new BigInteger("0");
		this.s=new BigInteger("0");
	}
	public void setSign(BigInteger r,BigInteger s){
		this.r=r;
		this.s=s;
	}
}

class SHA1{
	private BigInteger bigInt;
	public SHA1(String s) throws NoSuchAlgorithmException
	{
		String result="";
		MessageDigest MD=MessageDigest.getInstance("SHA-1");
		MD.update(s.getBytes());
		byte by0[]=MD.digest();
		for (int i = 0; i < by0.length; i++) {
		    String tmp = Integer.toHexString(by0[i] & 0xFF);
		    if (tmp.length() == 1) {
			result += "0" + tmp;
		    } else {
			result += tmp;
		    }
		}		
		bigInt = new BigInteger(result,16);
	}
	public BigInteger getSHA(){	return bigInt;	}
}

public class DSS {
	static int L;
	static int g;
	static BigInteger number0=new BigInteger("0");
	static BigInteger number1=new BigInteger("1");
	static BigInteger number2=new BigInteger("2");
	static PK publickey=new PK();
	static SK secretkey=new SK();
	static DSASignature Dsign=new DSASignature();
	public static void genkey() throws NoSuchAlgorithmException{
		BigInteger S0 = new BigInteger("0");
		BigInteger P = new BigInteger("0");
		BigInteger Q = new BigInteger("0");
		BigInteger G = new BigInteger("0");
		BigInteger X = new BigInteger("0");
		BigInteger Y = new BigInteger("0");
		BigInteger Xor = new BigInteger("0");
		int n=(L-1)/g;
		int b=(L-1)%g;
		SecureRandom random=new SecureRandom();
		boolean t=false;
		while (!t){
		boolean t1=false;
		while (!t1){
		S0=new BigInteger(g,100,random);
		BigInteger S1=new BigInteger("1");
		S1=S1.add(S0);
		BigInteger R1=new SHA1(S0.toString()).getSHA();
		BigInteger R2=new SHA1(S1.toString()).getSHA();
		BigInteger m = number2.pow(g);
		Xor=R1.xor(R2.mod(m));		
		if (Xor.mod(number2).intValue()==0) {Xor=Xor.add(number1);}
		m=number2.pow(g-1);
		if (Xor.compareTo(m)==-1){ Xor=Xor.add(m);}
		t1=Xor.isProbablePrime(100);
		}
		//System.out.println(Xor);
		Q=Xor;
		boolean t2=false;
		int C=0;
		BigInteger N=new BigInteger("2");
		while (!t2){
			BigInteger W=new BigInteger("0");
			BigInteger m=number2.pow(g);
			m = number2.pow(g);
			for (int k=0;k<=n;k++){
				BigInteger K;
				K=BigInteger.valueOf(k);
				BigInteger temp=new BigInteger(S0.toString());
				temp=temp.add(N).add(K);
				temp=temp.mod(m);
				temp=new SHA1(temp.toString()).getSHA();
				if (k==n) temp=temp.mod(number2.pow(b));
				W=W.add(temp.multiply(number2.pow(k*g)));
				//System.out.println(W);
			}
			//System.out.println(W);
			X=W.add(number2.pow(L-1));
			//System.out.println(X);
			P=X;
			BigInteger q=Q.multiply(number2);
			P=P.subtract(X.mod(q));
			//System.out.println(P.subtract(X.mod(q)));
			P=P.add(number1);
			//System.out.println(P.compareTo(number2.pow(L-1)));
			//System.out.println(P.compareTo(number2.pow(L)));
			if ((P.compareTo(number2.pow(L-1))==1)
					&&(P.isProbablePrime(100)))
					//	&(P.mod(Q.multiply(number2))==number1)) 
							{t=true;t2=true;}
			if (C==4096) {t2=true;}
			C=C+1;
			N=N.add(BigInteger.valueOf(n+1));

		}
		}
		BigInteger h=new BigInteger(100,random);
		BigInteger g=P.subtract(number1).divide(Q);
		G=h.modPow(g, P);
		X=new BigInteger(100,random);
		Y=G.modPow(X, P);
		//System.out.println(P);
		//System.out.println(Q);
		//System.out.println(G);
		//System.out.println(X);
		//System.out.println(Y);
		publickey.setPK(P, Q, G, Y);
		secretkey.setSK(P, Q, G, X);
	}

	public static DSASignature sign(SK Skey,String M) throws NoSuchAlgorithmException{
		SecureRandom random=new SecureRandom();
		BigInteger K=new BigInteger(100,random);
		K=K.add(number1);
		DSASignature rs=new DSASignature();
		BigInteger r=Skey.g.modPow(K, Skey.p);
		r=r.mod(Skey.q);
		BigInteger SHAM=new SHA1(M).getSHA();
		BigInteger s=r.multiply(Skey.x);
		s=s.add(SHAM);
		K=K.modInverse(Skey.q);
		s=s.multiply(K).mod(Skey.q);
		rs.r=r;rs.s=s;
		return rs;
	}
	
	public static boolean verify(String M,PK Pkey,DSASignature Sign) throws NoSuchAlgorithmException{
		BigInteger W=Sign.s.modInverse(Pkey.q);
		BigInteger u1=new SHA1(M).getSHA();
		u1=u1.multiply(W);u1=u1.mod(Pkey.q);
		BigInteger u2=Sign.r;
		u2=u2.multiply(W);u2=u2.mod(Pkey.q);
		BigInteger V1,V2,V;
		V1=Pkey.g.modPow(u1, Pkey.p);
		V2=Pkey.y.modPow(u2, Pkey.p);
		V=V1.multiply(V2);V=V.mod(Pkey.p);V=V.mod(Pkey.q);
		return Sign.r.equals(V);
	}
	
	public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
		if (args.length<5) {
	        System.out.println("input error: information not complete");
	        System.exit(0);
	      }
		
		if (args[0].equals("-p")){
			boolean t=true;
			L=Integer.parseInt(args[1]);
			if (args[2].equals("-q")) {g=Integer.parseInt(args[3]);} else t=false;
			boolean checksize=false;
			if (L==1024&&g==160) checksize=true;
			if (L==2048&&g==224) checksize=true;
			if (L==2048&&g==256) checksize=true;
			if (L==3072&&g==256) checksize=true;
			if (!checksize)
			{
				System.out.println("Input Error: size of p,q is not available!");
				System.exit(0);
			}
			genkey();
			if (args[4].equals("-S")) {
				Encoder pe=new Encoder(secretkey.p);
				Encoder qe=new Encoder(secretkey.q);
				Encoder ge=new Encoder(secretkey.g);
				Encoder xe=new Encoder(secretkey.x);
				Encoder my_seq = (new Encoder()).initSequence()
						.addToSequence(pe)
						.addToSequence(qe)
						.addToSequence(ge)
						.addToSequence(xe)
						;
				FileOutputStream Sfile = new FileOutputStream(args[5]);
				Sfile.write(my_seq.getBytes());
				Sfile.close();
			//	System.out.println(my_seq);
			}else t=false;
			if (args[6].equals("-P")){
				FileOutputStream Pfile = new FileOutputStream(args[7]);
				Encoder pe=new Encoder(publickey.p);
				Encoder qe=new Encoder(publickey.q);
				Encoder ge=new Encoder(publickey.g);
				Encoder ye=new Encoder(publickey.y);
				Encoder my_seq = (new Encoder()).initSequence()
						.addToSequence(pe)
						.addToSequence(qe)
						.addToSequence(ge)
						.addToSequence(ye)
						;
				Pfile.write(my_seq.getBytes());
				Pfile.close();
				//System.out.println(my_seq);
			}else t=false;
			if (!t) {
				System.out.println("Input error: information not complete");
				System.exit(0);
			}
			secretkey=new SK();
			publickey=new PK();
			System.out.println("Key Generation Completed,   SK_file:"+args[5]+"  PK_file:"+args[7]);
			}
		if (args[0].equals("-M")){
			byte b[]=new byte[1];
			boolean t=true;
			FileInputStream Mfile = new FileInputStream(args[1]);
			String M="";
			SK Skey=new SK();
			while (Mfile.read(b)!=-1)
			{
				String temp=new String(b);
				M=M+temp;
			}
			Mfile.close();
			if ((!args[2].equals("-S"))&&(!args[2].endsWith("-P"))) t=false;
			if (args[2].equals("-S")){
				FileInputStream Sfile = new FileInputStream(args[3]);
				byte[] b2=new byte[1000000];
				int length=0;
				while (Sfile.read(b)!=-1)
				{
					b2[length]=b[0];
					length++;
				}length++;
				Sfile.close();
				Decoder dec = new Decoder(b2,0,length);
				dec=dec.getContent();
				Skey.setSK(dec.getFirstObject(true).getInteger(), 
						   dec.getFirstObject(true).getInteger(),
						   dec.getFirstObject(true).getInteger(),
						   dec.getFirstObject(true).getInteger());
				DSASignature DSAsign = sign(Skey,M);
			if (args[4].equals("-s")){
				FileOutputStream sfile = new FileOutputStream(args[5]);
				Encoder re=new Encoder(DSAsign.r);
				Encoder se=new Encoder(DSAsign.s);
				Encoder my_seq = (new Encoder()).initSequence()
						.addToSequence(re)
						.addToSequence(se)
						;
				sfile.write(my_seq.getBytes());
				sfile.close();
			}else t=false;
			System.out.println("Sign completed!  MessageFile:"+args[1]+"  SignFile:"+args[5]);
			}
			PK Pkey=new PK();
			if (args[2].equals("-P")){
				FileInputStream Pfile = new FileInputStream(args[3]);
				byte[] b2=new byte[1000000];
				int length=0;
				while (Pfile.read(b)!=-1)
				{
					b2[length]=b[0];
					length++;
				}length++;
				Pfile.close();
				Decoder dec = new Decoder(b2,0,length);
				dec=dec.getContent();
				Pkey.setPK(dec.getFirstObject(true).getInteger(), 
						   dec.getFirstObject(true).getInteger(),
						   dec.getFirstObject(true).getInteger(),
						   dec.getFirstObject(true).getInteger());
				DSASignature DS = new DSASignature();
				if (args[4].equals("-s")){
					FileInputStream sfile = new FileInputStream(args[5]);
					length=0;
					while (sfile.read(b)!=-1)
					{
						b2[length]=b[0];
						length++;
					}length++;
					sfile.close();
					dec = new Decoder(b2,0,length);
					dec = dec.getContent();
					DS.setSign(dec.getFirstObject(true).getInteger(),
							   dec.getFirstObject(true).getInteger());
					boolean ver=verify(M,Pkey,DS);
					if (ver) System.out.println("Verify Completed! The Signature is available!");
					else System.out.println("Verify Failed! The Signature is not available!");
				}
			}
			if (!t) {
				System.out.println("Input error: information not complete");
				System.exit(0);
			}
		}
	}
}

