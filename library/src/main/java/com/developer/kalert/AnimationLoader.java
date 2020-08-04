package com.developer.kalert;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.animation.*;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;

class AnimationLoader {

    static Animation loadAnimation(Context context, int id)
            throws Resources.NotFoundException {

        try (XmlResourceParser parser = context.getResources().getAnimation(id)) {
            return createAnimationFromXml(context, parser);
        } catch (XmlPullParserException | IOException ex) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                throw new Resources.NotFoundException("Can't load animation resource ID #0x" +
                        Integer.toHexString(id), ex);
            }
        }
        return null;
    }

    private static Animation createAnimationFromXml(Context c, XmlPullParser parser)
            throws XmlPullParserException, IOException {

        return createAnimationFromXml(c, parser, null, Xml.asAttributeSet(parser));
    }

    private static Animation createAnimationFromXml(Context c, XmlPullParser parser,
                                                    AnimationSet parent, AttributeSet attrs) throws XmlPullParserException, IOException {

        Animation anim = null;

        // Make sure we are on a start tag.
        int type;
        int depth = parser.getDepth();

        while (((type=parser.next()) != XmlPullParser.END_TAG || parser.getDepth() > depth)
                && type != XmlPullParser.END_DOCUMENT) {

            if (type != XmlPullParser.START_TAG) {
                continue;
            }

            String  name = parser.getName();

            switch (name) {
                case "set":
                    anim = new AnimationSet(c, attrs);
                    createAnimationFromXml(c, parser, (AnimationSet) anim, attrs);
                    break;
                case "alpha":
                    anim = new AlphaAnimation(c, attrs);
                    break;
                case "scale":
                    anim = new ScaleAnimation(c, attrs);
                    break;
                case "rotate":
                    anim = new RotateAnimation(c, attrs);
                    break;
                case "translate":
                    anim = new TranslateAnimation(c, attrs);
                    break;
                default:
                    try {
                        anim = (Animation) Class.forName(name).getConstructor(Context.class, AttributeSet.class).newInstance(c, attrs);
                    } catch (Exception te) {
                        throw new RuntimeException("Unknown animation name: " + parser.getName() + " error:" + te.getMessage());
                    }
                    break;
            }

            if (parent != null) {
                parent.addAnimation(anim);
            }
        }

        return anim;

    }
}
