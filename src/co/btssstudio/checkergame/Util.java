package co.btssstudio.checkergame;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

public class Util {
	public static String pathToString(String path) {
		try {
			File f = new File(path);
			BufferedReader br = new BufferedReader(new FileReader(f));
			StringBuilder sb = new StringBuilder();
			String s;
			while((s = br.readLine()) != null) {
				sb.append(s + "\n");
			}
			br.close();
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	public static ByteBuffer fillBufferWithByte(byte[] array) {
		ByteBuffer buffer = ByteBuffer.allocateDirect(array.length).order(ByteOrder.nativeOrder());
		buffer.put(array).flip();
		return buffer;
	}
	public static FloatBuffer fillBufferWithFloat(float[] array) {
		FloatBuffer buffer = ByteBuffer.allocateDirect(array.length << 2).order(ByteOrder.nativeOrder()).asFloatBuffer();
		buffer.put(array).flip();
		return buffer;
	}
	public static IntBuffer fillBufferWithInt(int[] array) {
		IntBuffer buffer = ByteBuffer.allocateDirect(array.length << 2).order(ByteOrder.nativeOrder()).asIntBuffer();
		buffer.put(array).flip();
		return buffer;
	}
	public static class ImageData {
		public int[] imageData;
		public int width;
		public int height;
		public ImageData(int[] imageData, int w, int h) {
			this.imageData = imageData;
			width = w;
			height = h;
		}
		public int[] getData() {
			return imageData;
		}
		public int getWidth() {
			return width;
		}
		public int getHeight() {
			return height;
		}
	}
	public static ImageData readImageData(String path) {
		try {
			BufferedImage src = ImageIO.read(new FileInputStream(path));
			int width = src.getWidth();
			int height = src.getHeight();
			int[] pixels = new int[width * height];
			src.getRGB(0, 0, width, height, pixels, 0, width);
			int[] imageData = new int[width * height];
			for(int i = 0 ; i < imageData.length ; i++) {
				int a = (pixels[i] & 0xff000000) >> 24;
				int r = (pixels[i] & 0xff0000) >> 16;
				int g = (pixels[i] & 0xff00) >> 8;
				int b = (pixels[i] & 0xff);
				imageData[i] = a << 24 | b << 16 | g << 8 | r;
			}
			return new ImageData(imageData, width, height);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
