import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * @Title: GifExample.java
 * @Package test
 * @Description: TODO(添加描述)
 * @author Administrator
 * @date 2012-7-12 下午3:39:21
 * @version V1.0
 */
public class GifExample {
	public static void main(String[] args) throws IOException {

		boolean ok = ImageIO.write(createImage(), "gif", new File("C://test.gif"));

		System.out.println("success=" + ok);

	}

	static BufferedImage createImage() {

		IndexColorModel cm = createIndexColorModel();

		BufferedImage im = new BufferedImage(100, 100, BufferedImage.TYPE_BYTE_INDEXED, cm);

		Graphics2D g = im.createGraphics();

//		g.setColor(new Color(0, 0, 0, 0)); // transparent
//
//		g.fillRect(0, 0, 100, 100);

//		g.setColor(Color.RED);
//
//		g.fillRect(0, 0, 50, 50);
//
//		g.setColor(Color.GREEN);
//
//		g.fillRect(50, 50, 50, 50);
//
//		g.dispose();

		return im;

	}

	static IndexColorModel createIndexColorModel() {

		BufferedImage ex = new BufferedImage(1, 1, BufferedImage.TYPE_BYTE_INDEXED);

		IndexColorModel icm = (IndexColorModel) ex.getColorModel();

		int SIZE = 256;

		byte[] r = new byte[SIZE];

		byte[] g = new byte[SIZE];

		byte[] b = new byte[SIZE];

		byte[] a = new byte[SIZE];

		icm.getReds(r);

		icm.getGreens(g);

		icm.getBlues(b);

		java.util.Arrays.fill(a, (byte) 255);

		r[0] = g[0] = b[0] = a[0] = 0; // transparent

		return new IndexColorModel(8, SIZE, r, g, b, a);

	}

}
