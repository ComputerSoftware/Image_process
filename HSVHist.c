#include "stdafx.h"
#include <cv.h>
#include <highgui.h>

int main(int argc, char** argv)//用来向main函数传递参数
{
	IplImage * src = cvLoadImage("picture.jpg");//加载一幅图像
	IplImage* hsv = cvCreateImage(cvGetSize(src), 8, 3);//hsv色彩空间(hue色相 saturation饱和度 value明度)
	IplImage* h_plane = cvCreateImage(cvGetSize(src), 8, 1);
	IplImage* s_plane = cvCreateImage(cvGetSize(src), 8, 1);
	IplImage* v_plane = cvCreateImage(cvGetSize(src), 8, 1);
	IplImage* planes[] = { h_plane, s_plane };


	int h_bins = 16, s_bins = 8;
	int hist_size[] = { h_bins, s_bins };


	float h_ranges[] = { 0, 180 };


	float s_ranges[] = { 0, 255 };
	float* ranges[] = { h_ranges, s_ranges };


	cvCvtColor(src, hsv, CV_BGR2HSV);
	cvCvtPixToPlane(hsv, h_plane, s_plane, v_plane, 0);


	CvHistogram * hist = cvCreateHist(2, hist_size, CV_HIST_ARRAY, ranges, 3);

	cvCalcHist(planes, hist, 0, 0);


	float max_value;
	cvGetMinMaxHistValue(hist, 0, &max_value, 0, 0);



	int height = 240;
	int width = (h_bins*s_bins * 6);
	IplImage* hist_img = cvCreateImage(cvSize(width, height), 8, 3);
	cvZero(hist_img);


	IplImage * hsv_color = cvCreateImage(cvSize(1, 1), 8, 3);
	IplImage * rgb_color = cvCreateImage(cvSize(1, 1), 8, 3);
	int bin_w = width / (h_bins * s_bins);
	for (int h = 0; h < h_bins; h++)
	{
		for (int s = 0; s < s_bins; s++)
		{
			int i = h*s_bins + s;

			float bin_val = cvQueryHistValue_2D(hist, h, s);
			int intensity = cvRound(bin_val*height / max_value);


			cvSet2D(hsv_color, 0, 0, cvScalar(h*180.f / h_bins, s*255.f / s_bins, 255, 0));
			cvCvtColor(hsv_color, rgb_color, CV_HSV2BGR);
			CvScalar color = cvGet2D(rgb_color, 0, 0);

			cvRectangle(hist_img, cvPoint(i*bin_w, height),
				cvPoint((i + 1)*bin_w, height - intensity),
				color, -1, 8, 0);
		}
	}

	cvNamedWindow("Source", 1);//创建一个"Source"窗口
	cvShowImage("Source", src);//在"Source"窗口中显示图像

	cvNamedWindow("H-S Histogram", 1);//创建一个"H-S Histogtam"窗口
	cvShowImage("H-S Histogram", hist_img);//在"H-S Histogtam"窗口中显示图像

	cvWaitKey(0);//等待输入
}
