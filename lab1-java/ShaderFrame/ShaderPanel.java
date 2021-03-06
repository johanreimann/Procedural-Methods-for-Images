/*
 * A JPanel to display a procedural pattern,
 * generated by a very simple external Shader object.
 *
 * Demo code used for teaching.
 *
 * Author: Stefan Gustavson, ITN-LiTH, 2006-10-24
 *
 */

import java.awt.*;
import java.awt.image.*;
import javax.swing.*;

public class ShaderPanel extends JPanel implements Runnable {

	// The shader to execute when rendering pixels
    public Shader myShader; // A very simple class with one method: shader()

    // Quite a few class variables - not a very clean solution, but
    // we don't really want to pass all of these around as arguments.
    // (This is a pretty ugly framework, it's really just a quick hack.)
    private int width;
    private int height;
    private long startTime;
    private int pixels[];
    private Thread myThread;
    private MemoryImageSource mySource;
    private Image myImage;
    
	// Constructor: Set up the back buffer image and the pixel data source
    ShaderPanel(int w, int h) {
    	super(); // Create the JPanel
        width = w;
        height = h;
        setPreferredSize(new Dimension(width, height));
        // Create a large array for the RGB pixel data
        pixels = new int[width*height];
        for (int i = 0; i < width*height; i++)
            pixels[i]=0xff000000;  // Fill with opaque black (A=1, R=G=B=0)
        // Create an ImageSource, to make an Image out of the pixel data
        mySource = new MemoryImageSource(width, height, pixels, 0, width);
        // Tell Java that the image source will be continuously updated in memory
        mySource.setAnimated(true);
        // Create an Image taking its pixel data from the ImageSource
        myImage = createImage(mySource);
    }

	// If no size is given, create 512x512 pixel panel
    ShaderPanel() {
    	this(512, 512);
    }

    // Convert an RGB color vector (in a double[3] array) to a 32-bit integer.
    // The pixel data is 4x8 bits packed as "AAAAAAAARRRRRRRRGGGGGGGGBBBBBBBB".
    int packRGB(double[] RGB) {
		// Rescale from 0..1 to 0..255 and quantize to integers
        int r = (int)(RGB[0]*255.0);
        int g = (int)(RGB[1]*255.0);
        int b = (int)(RGB[2]*255.0);

		// Clamp to 0..255
        if(r>255) r = 255;
        if(g>255) g = 255;
        if(b>255) b = 255;
        if(r<0) r = 0;
        if(g<0) g = 0;
        if(b<0) b = 0;

        // This is equivalent to, but faster than:
        // return 255*16777216 + r*65536+ g*256 + b;
        return 0xff000000 | (r<<16) | (g<<8) | b;
    }

    // Update the image in a continuous loop, in a separate thread
    public void run() {
		startTime = System.currentTimeMillis();
    	while(true) { // Loop forever (until program exits)
	    	render(); // Compute a new procedural image
			try {
    		Thread.sleep(40); // Delay and leave some power for other programs
			} catch(InterruptedException e) {};
    	}
    }

    // Render the pixels for the image
    void render()
    {
        double[] pixelcolor = {0.0, 0.0, 0.0};
        double u, v, t;
        // Get the current time in seconds
		t = (double)(System.currentTimeMillis()-startTime)/1000.0;

        for(int y=0; y<height; y++)
            {
                for(int x = 0; x<width; x++)
                    {
                    	u = (double)x/width;
                    	v = (double)y/height;
                        // Render one pixel by invoking myShader.shader()
                        myShader.shader(pixelcolor, u,v,t);
                        // Write the color to the pixels[] array
                        pixels[y*width+x]=packRGB(pixelcolor);
                    }
            }
            // Mark the entire image as newly updated, and repaint
       	    mySource.newPixels(0,0,width,height);
           	repaint();
    }

	// This is where we actually draw the image to the window
    public void paintComponent(Graphics g)
    {
        if(myImage != null)
            g.drawImage(myImage, 0, 0, width, height, this);
    }

}
