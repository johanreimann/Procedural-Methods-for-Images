// A simple SL surface shader
surface pine_surface() {

  color treecolor = color(0.0, 0.45, 0.0); // Dark green
  color snowcolor = color(0.9, 0.9, 0.9); // Color of snow
  color Cd = treecolor;

  float colortop = filterstep(0, N.vector(0,1,0)); // filterstep to draw snow while the surface is directed upwards.
  Cd = mix(treecolor, snowcolor, colortop);

  Ci = Cd * diffuse(N);
}

