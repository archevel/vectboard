package com.hitvol.vectboard;

/**
 * Created by archevel on 2015-11-15.
 */
public class PathMaker {

    public static final String PATH_START = "<path ";
    public static final String PATH_COMMAND_START = "d=\"M";
    public static final String PATH_COMMAND_END = "\" ";
    public static final String PATH_END = "fill=\"none\" stroke=\"black\"/>";

    public String asPathTag(Filament f) {
        StringBuilder sb = new StringBuilder();
        sb.append(PATH_START);

        if (f.getPoints().size() > 0) {
            sb.append(PATH_COMMAND_START);
            for (Filament.Point p : f.getPoints()) {
                sb.append(" ");
                sb.append(p.x);
                sb.append(" ");
                sb.append(p.y);
            }

            sb.append(PATH_COMMAND_END);
        }

        sb.append(PATH_END);

        return sb.toString();
    }
}
