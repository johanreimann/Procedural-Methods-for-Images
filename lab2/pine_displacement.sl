// A simple SL displacement shader

displacement pine_displacement() {
  float branches = noise(5*P)-0.5; // Makes the surface of the cone uneven
  branches += 1.5 * noise(40*P)-0.7; // Creates branches

  branches *= smoothstep(0.0,0.1,1-v); // Removes branches at the top of the tree

  P = P + N * 0.3 * branches;
  N = calculatenormal(P);
}