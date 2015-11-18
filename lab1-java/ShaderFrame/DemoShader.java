public class DemoShader extends Shader {

	void shader(double[] p, double u, double v, double t) {

		double g, wave;


		// if(v < 0.8) wave = 1;
		// else {
		// 	wave = ((u*u*u)*2)-((u*u)*3);
		// }
		wave = ((u*u*u)*3)-((u*u)*Math.cos(t*1.5)*1.5);
		g = 0.5 + 0.5*SimplexNoise.noise((Math.cos(u-0.5))*16.0, (0.5-u)*16.0-(0.5-v)*4.0*wave, t*0.2);
		// g2 = 0.5 + 0.5*SimplexNoise.noise((u-0.5)*16.0, (0.5-v)*16.0, t*0.2);
		// g3 = 0.5 + 0.5*SimplexNoise.noise((u-0.5)*16.0, (0.5-v)*16.0, t*0.2);
		p[0]=g*0.7;
		p[1]=g*0.2;
		p[2]=g*0;
	}

}
