uniform sampler2D texture;
void main()
{
 	vec4 color, texel;
    color = gl_Color;
	if(gl_TexCoord[0].x <= 1)
	{
		texel = texture2D(texture, gl_TexCoord[0].st);
		gl_FragColor = texel;
	}
	else
	{	
		gl_FragColor = gl_FragColor * 0;   
	}
	
}