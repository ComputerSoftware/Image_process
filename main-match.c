
/**************************************************************************************************/
/* Copyright (C) xiaowei_cqu, 2014-2015                                                           */
/*                                                                                                */
/*  FILE NAME             :  main-match.c                                                          */
/*  PRINCIPAL AUTHOR      :  xiaowei_cqu                                                               */
/*  SUBSYSTEM NAME        :  main-match                                                            */
/*  MODULE NAME           :  main-match function                                                   */
/*  LANGUAGE              :  C                                                                    */
/*  TARGET ENVIRONMENT    :  ANY                                                                  */
/*  DATE OF FIRST RELEASE :  2014/10/23                                                           */
/*  DESCRIPTION           :  This is a main-match source program                                   */
/**************************************************************************************************/

/*
* Revision log:
*
* Created by xiaowei_cqu ,updated by Lixing, 2014/10/23
*
*/

#include <opencv2/opencv.hpp>
#include <iostream>
#include <stdio.h>
#include <stdarg.h>
#define cvQueryHistValue_1D( hist, idx0 ) \
	((float)cvGetReal1D((hist)->bins, (idx0)))
using namespace std;
using namespace cv;

void myShowHist(char* name, IplImage* img);
void myHistMatch(IplImage *histimg, double histv[]);
void GenerateGaussModel(double model[]);

int main()
{
	IplImage * image = cvLoadImage("E:/image-database/11.jpg");
	IplImage* redImage = cvCreateImage(cvGetSize(image), image->depth, 1);
	IplImage* greenImage = cvCreateImage(cvGetSize(image), image->depth, 1);
	IplImage* blueImage = cvCreateImage(cvGetSize(image), image->depth, 1);
	cvSplit(image, blueImage, greenImage, redImage, NULL);//图象通道分离

	IplImage* matchimg = cvCreateImage(cvGetSize(image), image->depth, 3);
	double model[256] = { 0 };
	GenerateGaussModel(model);
	myHistMatch(redImage, model);
	myHistMatch(greenImage, model);
	myHistMatch(blueImage, model);
	cvMerge(blueImage, greenImage, redImage, NULL, matchimg);


	IplImage * image2 = cvLoadImage("E:/image-database/12.jpg");
	int hist_size = 256;
	float range[] = { 0, 255 };
	float* ranges[] = { range };

	IplImage* gray_plane = cvCreateImage(cvGetSize(image), 8, 1);
	cvCvtColor(image, gray_plane, CV_BGR2GRAY);
	CvHistogram* gray_hist = cvCreateHist(1, &hist_size, CV_HIST_ARRAY, ranges, 1);
	cvCalcHist(&gray_plane, gray_hist, 0, 0);


	IplImage* gray_plane2 = cvCreateImage(cvGetSize(image2), 8, 1);
	cvCvtColor(image2, gray_plane2, CV_BGR2GRAY);
	CvHistogram* gray_hist2 = cvCreateHist(1, &hist_size, CV_HIST_ARRAY, ranges, 1);
	cvCalcHist(&gray_plane2, gray_hist2, 0, 0);

	//相关：CV_COMP_CORREL    
	//卡方：CV_COMP_CHISQR
	//直方图相交：CV_COMP_INTERSECT
	//Bhattacharyya距离：CV_COMP_BHATTACHARYYA
	double  com = cvCompareHist(gray_hist, gray_hist2, CV_COMP_BHATTACHARYYA);

	cout << com << endl;

	IplImage* matchimg2 = cvCreateImage(cvGetSize(image2), image->depth, 3);
	IplImage* redImage2 = cvCreateImage(cvGetSize(image2), image->depth, 1);
	IplImage* greenImage2 = cvCreateImage(cvGetSize(image2), image->depth, 1);
	IplImage* blueImage2 = cvCreateImage(cvGetSize(image2), image->depth, 1);
	cvSplit(image2, blueImage2, greenImage2, redImage2, NULL);

	myHistMatch(redImage2, model);
	myHistMatch(greenImage2, model);
	myHistMatch(blueImage2, model);
	cvMerge(blueImage2, greenImage2, redImage2, NULL, matchimg2);

	IplImage* gray_match = cvCreateImage(cvGetSize(matchimg), 8, 1);
	cvCvtColor(matchimg, gray_match, CV_BGR2GRAY);
	cvCalcHist(&gray_match, gray_hist, 0, 0);

	IplImage* gray_match2 = cvCreateImage(cvGetSize(matchimg2), 8, 1);
	cvCvtColor(matchimg2, gray_match2, CV_BGR2GRAY);
	cvCalcHist(&gray_match2, gray_hist2, 0, 0);
	myShowHist("Match1", matchimg);
	myShowHist("Match2", matchimg2);
	double  com2 = cvCompareHist(gray_hist, gray_hist2, CV_COMP_BHATTACHARYYA);

	cout << com2 << endl;


	//

	//IplImage* eqlimage=cvCreateImage(cvGetSize(image),image->depth,3);
	//

	//cvEqualizeHist(redImage,redImage);
	//cvEqualizeHist(greenImage,greenImage); 
	//cvEqualizeHist(blueImage,blueImage); 

	//cvMerge(blueImage,greenImage,redImage,NULL,eqlimage);

	//myShowHist("Equalized",eqlimage);




	//cvSplit(image,blueImage,greenImage,redImage,NULL);

	//
	//myShowHist("Matched",matchimg);
}

//将图像与特定函数分布histv[]匹配
void myHistMatch(IplImage *img, double histv[])
{
	int bins = 256;
	int sizes[] = { bins };
	CvHistogram *hist = cvCreateHist(1, sizes, CV_HIST_ARRAY);
	cvCalcHist(&img, hist);
	cvNormalizeHist(hist, 1);
	double val_1 = 0.0;
	double val_2 = 0.0;
	uchar T[256] = { 0 };
	double S[256] = { 0 };
	double G[256] = { 0 };
	for (int index = 0; index<256; ++index)
	{
		val_1 += cvQueryHistValue_1D(hist, index);
		val_2 += histv[index];
		G[index] = val_2;
		S[index] = val_1;
	}

	double min_val = 0.0;
	int PG = 0;
	for (int i = 0; i<256; ++i)
	{
		min_val = 1.0;
		for (int j = 0; j<256; ++j)
		{
			if ((G[j] - S[i]) < min_val && (G[j] - S[i]) >= 0)
			{
				min_val = (G[j] - S[i]);
				PG = j;
			}

		}
		T[i] = (uchar)PG;
	}

	uchar *p = NULL;
	for (int x = 0; x<img->height; ++x)
	{
		p = (uchar*)(img->imageData + img->widthStep*x);
		for (int y = 0; y<img->width; ++y)
		{
			p[y] = T[p[y]];
		}
	}
}

// 生成高斯分布
void GenerateGaussModel(double model[])
{
	double m1, m2, sigma1, sigma2, A1, A2, K;
	m1 = 0.15;
	m2 = 0.75;
	sigma1 = 0.05;
	sigma2 = 0.05;
	A1 = 1;
	A2 = 0.07;
	K = 0.002;

	double c1 = A1*(1.0 / (sqrt(2 * CV_PI))*sigma1);
	double k1 = 2 * sigma1*sigma1;
	double c2 = A2*(1.0 / (sqrt(2 * CV_PI))*sigma2);
	double k2 = 2 * sigma2*sigma2;
	double p = 0.0, val = 0.0, z = 0.0;
	for (int zt = 0; zt < 256; ++zt)
	{
		val = K + c1*exp(-(z - m1)*(z - m1) / k1) + c2*exp(-(z - m2)*(z - m2) / k2);
		model[zt] = val;
		p = p + val;
		z = z + 1.0 / 256;
	}
	for (int i = 0; i<256; ++i)
	{
		model[i] = model[i] / p;
	}
}


void myShowHist(char* name, IplImage* img)
{
	int hist_size = 256;
	int hist_height = 256;
	float range[] = { 0, 255 };
	float* ranges[] = { range };
	IplImage* gray_plane = cvCreateImage(cvGetSize(img), 8, 1);
	cvCvtColor(img, gray_plane, CV_BGR2GRAY);

	CvHistogram* gray_hist = cvCreateHist(1, &hist_size, CV_HIST_ARRAY, ranges, 1);
	cvCalcHist(&gray_plane, gray_hist, 0, 0);
	cvNormalizeHist(gray_hist, 1.0);
	float scale1 = 1.5;

	int  x = img->width;
	int y = img->height;
	int max = (x > y) ? x : y;
	float scale2 = (float)((float)max / hist_size);


	IplImage* hist_image = cvCreateImage(cvSize((int)(x / scale2 + hist_size*scale1),
		(int)(hist_height)), 8, 3);
	cvZero(hist_image);

	cvSetImageROI(hist_image, cvRect(0, 0, (int)(x / scale2), (int)(y / scale2)));
	cvResize(img, hist_image);
	cvResetImageROI(hist_image);

	cvSetImageROI(hist_image, cvRect((int)(x / scale2), 0, hist_size*scale1, hist_height));
	float max_value = 0;
	cvGetMinMaxHistValue(gray_hist, 0, &max_value, 0, 0);
	for (int i = 0; i<hist_size; i++)
	{
		float bin_val = cvQueryHistValue_1D(gray_hist, i);
		int intensity = cvRound(bin_val*hist_height / max_value);
		cvRectangle(hist_image,
			cvPoint(i*scale1, hist_height - 1),
			cvPoint((i + 1)*scale1 - 1, hist_height - intensity),
			CV_RGB(255, 255, 255));
	}
	cvResetImageROI(hist_image);

	cvNamedWindow(name, 1);
	cvShowImage(name, hist_image);
	cvWaitKey(0);
	//cvDestroyWindow(name);
}