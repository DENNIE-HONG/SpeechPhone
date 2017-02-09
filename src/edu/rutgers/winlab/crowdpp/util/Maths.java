/*
 * Copyright (c) 2015-2016 Luyan Hong
 * This file is part of the Crowdpp.
 *
 * Crowdpp is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Crowdpp is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with the Crowdpp. If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package edu.rutgers.winlab.crowdpp.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.ejml.simple.SimpleMatrix;

/**
 * The Maths class 
 * A collection of used linear algebra and statistics functions
 * @author Luyan Hong
 */

public class Maths {

	/** @return the 2 norm */
	public static double getNorm2(double[] array) {
		double norm = 0;
		for(int i = 0; i < array.length; i++){
			norm = norm + array[i] * array[i];
		}
		return Math.sqrt(norm);	
	}
	
	/** @return the dot product */
	public static double dotProduct(double[] array1, double[] array2) {
		double dot = 0;
		for(int i = 0; i < array1.length; i++){
			dot = dot + array1[i] * array2[i];
		}
		return dot;	
	}

	/** @return the column mean */
	public static double[] getColMean(double[][] array) {
		int cols = array[0].length;
		int rows = array.length;
		double[] colMean = new double[cols];
		for (int i = 0; i < cols; i++) {
			double mean = 0;
			for(int j = 0; j < rows; j++) {
				mean = mean + array[j][i];				
			}
			colMean[i] = mean / rows;
		}
		return (colMean);
	}
	/** @return the MFCC mean */
	public static double[] getMFCCMean(double[][] array) {
		int cols = array[0].length;
		int rows = array.length;
		double[] colMean = new double[cols];
		for (int i = 0; i < cols; i++) {
			double temp;
			for(int j = 0; j < rows; j++) {
				for(int k=0;k<rows-j-1;k++)
				{
					if(array[k][i]>array[k+1][i])
					{

				          temp=array[k][i];
				          array[k][i]=array[k+1][i];
				          array[k+1][i]=temp;
                    }
				}
				
			}
			if(rows%2==0)
			{   
				
				colMean[i] = (array[rows/2-1][i]+array[rows/2][i])/2;
			}
			else
			{
				colMean[i] = array[rows/2][i];
			}
		}
		return (colMean);
	}
	/** @return the pitch mean */
	public static double[] getPitchMean(double[][] array) {
		int cols = array[0].length;
		int rows = array.length;
		int rowsboy = 0;
		int rowsgirl= 0;
		double[] colMean = new double[cols];
		//double colmean1;
		//double colmean2;
		for (int i = 0; i < cols; i++) {
			double mean = 0;
			double meanboy = 0;
			double meangirl = 0;
			for(int j = 0; j < rows; j++) {
				if(array[j][i]!=-1)
				{
					mean = mean + array[j][i];
					if(array[j][i] <= 160)
						{
						   meanboy = meanboy + array[j][i];
						   rowsboy += 1;
						 }
					if(array[j][i]>=190)
					{
						meangirl = meangirl +array[j][i];
						rowsgirl += 1;
					}	
				}
				
				else
					{ j++;
					 rows=rows-1;}
			}
			if(rowsboy >= rowsgirl) 
				colMean[i] = meanboy/rowsboy;
			else 
			    colMean[i] = meangirl/rowsgirl;
		}
		return (colMean);
	}
	
	/** @return the column mean */
	public static double getpitchMean(SimpleMatrix dat) {
		int cols = dat.numCols();
		int rows = dat.numRows();
		List<Double> temp_list = new ArrayList<Double>();
		double mean;
		for (int i = 0; i < cols; i++) {
			for(int j = 0; j < rows; j++) {
				temp_list.add(dat.get(j,i));
			}
		}
		mean = Maths.getMean(temp_list);
		return mean;
	}
	
	/** @return the column mean */
	public static SimpleMatrix getColMean(SimpleMatrix dat) {
		int cols = dat.numCols();
		int rows = dat.numRows();
		double[] colmean = new double[cols];
		for (int i = 0; i < cols; i++) {
			double mean = 0;
			for(int j = 0; j < rows; j++) {
				mean = mean + dat.get(j,i);				
			}
			colmean[i] = mean / rows;
		}
		SimpleMatrix rv = new SimpleMatrix(cols,1,true,colmean);
		return (rv);
	}

	/** @return the mean */
	public static double getMean(List<Double> list) {
		double mean = 0;
		for (int i = 0; i < list.size(); i++) {
			mean = mean + list.get(i);
		}
		return (mean/list.size());
	}

	/** @return the mean */
	public static double getMean(double[] array) {
		double mean = 0;
		for (int i = 0; i < array.length; i++) {
			mean = mean + array[i];
		}
		return (mean/array.length);
	}

	/** @return the median */
	public static double getMedian(List<Double> list) {
		Collections.sort(list);		
		double median = 0;
		if (list.size() % 2 == 1) {
			median = list.get(list.size() / 2);
		}
		else {
			median = (list.get(list.size() / 2 - 1) + list.get(list.size() / 2)) / 2;
		}
		return median;		
	}
	
	/** @return the variance */
	public static double getVariance(List<Double> list) {
		double mean = getMean(list);
		double variance = 0;		
		for (int i = 0; i < list.size(); i++) {
			variance = variance + (list.get(i) - mean) * (list.get(i) - mean);
		}
		return (variance/list.size());
	}

	/** @return the variance */
	public static double getVariance(double[] array) {
		double mean = getMean(array);
		double variance = 0;		
		for (int i = 0; i < array.length; i++) {
			variance = variance + (array[i] - mean) * (array[i] - mean);
		}
		return (variance/array.length);
	}

	/** @return the diagonal covariance */
	public static SimpleMatrix getDiagonalCovariance(SimpleMatrix dat) {
		int p = dat.numCols();
		double []sigma = new double[p]; 
		for(int i = 0; i < p; i++) {
			SimpleMatrix temp_vec = dat.extractVector(false, i);
			List<Double> temp_list = new ArrayList<Double>();
			for(int j = 0; j < temp_vec.numRows(); j++) {
				temp_list.add(temp_vec.get(j));
			}
			sigma[i] = Math.sqrt(getVariance(temp_list));
		}
		SimpleMatrix sigma_mat = SimpleMatrix.diag(sigma);
		return sigma_mat;
	}
	
	/** @return the mfcc max */
	public static double getMFCCSum(SimpleMatrix dat) {
		SimpleMatrix mfcc_mean = Maths.getColMean(dat);
		int cols = mfcc_mean.numCols();
		int rows = mfcc_mean.numRows();
		double Sum = 0;
		for (int i = 0; i < cols; i++) {
			for(int j = 0; j < rows; j++) {
				Sum += mfcc_mean.get(j,i);
			}
			
		}
		return Sum;
	}
	
}
