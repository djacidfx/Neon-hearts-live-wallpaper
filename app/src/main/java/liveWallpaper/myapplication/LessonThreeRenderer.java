package liveWallpaper.myapplication;


public class LessonThreeRenderer extends LessonTwoRenderer {
    @Override 
    protected String getFragmentShader() {
        return "precision mediump float;       \nuniform vec3 u_LightPos;       \nvarying vec3 v_Position;\t\t\nvarying vec4 v_Color;          \nvarying vec3 v_Normal;         \nvoid main()                    \n{                              \n   float distance = length(u_LightPos - v_Position);                  \n   vec3 lightVector = normalize(u_LightPos - v_Position);             \n   float diffuse = max(dot(v_Normal, lightVector), 0.1);              \n   diffuse = diffuse * (1.0 / (1.0 + (0.25 * distance * distance)));  \n   gl_FragColor = v_Color * diffuse;                                  \n}                                                                     \n";
    }

    @Override 
    protected String getVertexShader() {
        return "uniform mat4 u_MVPMatrix;      \nuniform mat4 u_MVMatrix;       \nattribute vec4 a_Position;     \nattribute vec4 a_Color;        \nattribute vec3 a_Normal;       \nvarying vec3 v_Position;       \nvarying vec4 v_Color;          \nvarying vec3 v_Normal;         \nvoid main()                                                \n{                                                          \n   v_Position = vec3(u_MVMatrix * a_Position);             \n   v_Color = a_Color;                                      \n   v_Normal = vec3(u_MVMatrix * vec4(a_Normal, 0.0));      \n   gl_Position = u_MVPMatrix * a_Position;                 \n}                                                          \n";
    }
}
