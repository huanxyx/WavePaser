package pers.xyx.parser.test;

import pers.xyx.parser.wave.WaveFile;

public class TestWave {	
	public static void main(String[] args) throws Exception {
		WaveFile waveFile = new WaveFile("hello.wav");
	
		int[][] data = waveFile.getData();

		printData(data);
	}
	
	public static void printData(int data[][]){
		for(int i = 0; i < data[1].length; i++) {
			System.out.print(data[1][i] + " ");
			if((i+1) % 5 == 0) {
				System.out.println();
			}
		}

	}
	
	//int数组转换为double数组
	public static double[] Integer2Double(int [] data) {
		double[] result = new double[data.length];
		
		for(int i = 0; i < result.length; i++) {
			result[i] = data[i];
		}
		
		return result;
	}
}
