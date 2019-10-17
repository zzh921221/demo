package com.panicbuying.pic;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * @Title: GifTest.java
 * @Package test
 * @Description: TODO(添加描述)
 * @author Administrator
 * @date 2012-7-12 下午1:44:25
 * @version V1.0
 */
public class GifTest {
	/**
	 * @Title: main
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @author Administrator
	 * @param @param args 设定文件
	 * @return void 返回类型
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException {
		GifDecoder gd = new GifDecoder();
		int status = gd.read(new FileInputStream(new File("E:/pic/gal.gif")));
		if (status != GifDecoder.STATUS_OK) {
			return;
		}
		//

		AnimatedGifEncoder ge = new AnimatedGifEncoder();
		ge.start(new FileOutputStream(new File("E:/pic/gal2.gif")));
		ge.setRepeat(0);
		
		for (int i = 0; i < gd.getFrameCount(); i++) {
			BufferedImage frame = gd.getFrame(i);
			int width = frame.getWidth();
			int height = frame.getHeight();
			// 80%
			width = (int) (width * 0.25);
			height = (int) (height * 0.25);
			//
			BufferedImage rescaled = Scalr.resize(frame, Scalr.Mode.FIT_EXACT, width, height);
			//
			int delay = gd.getDelay(i);
			//

			ge.setDelay(delay);
			ge.addFrame(rescaled);
		}

		ge.finish();
		//
	}
	
	
}
