package com.kiosk.staticfunction;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by USER on 11/25/2015.
 */
public class Font {

    public static Typeface Bold(Context activity) {
        Typeface typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/OpenSans-Bold.ttf");
        return typeface;
    }

    public static Typeface BoldItalic(Context activity) {
        Typeface typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/OpenSans-BoldItalic.ttf");
        return typeface;
    }

    public static Typeface ExtraBold(Context activity) {
        Typeface typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/OpenSans-ExtraBold.ttf");
        return typeface;
    }

    public static Typeface ExtraBoldItalic(Context activity) {
        Typeface typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/OpenSans-ExtraBoldItalic.ttf");
        return typeface;
    }

    public static Typeface Italic(Context activity) {
        Typeface typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/OpenSans-Italic.ttf");
        return typeface;
    }

    public static Typeface Light(Context activity) {
        Typeface typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/OpenSans-Light.ttf");
        return typeface;
    }

    public static Typeface LightItalic(Context activity) {
        Typeface typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/OpenSans-LightItalic.ttf");
        return typeface;
    }

    public static Typeface Regular(Context activity) {
        Typeface typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/OpenSans-Regular.ttf");
        return typeface;
    }

    public static Typeface Semibold(Context activity) {
        Typeface typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/OpenSans-Semibold.ttf");
        return typeface;
    }

    public static Typeface SemiboldItalic(Context activity) {
        Typeface typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/OpenSans-SemiboldItalic.ttf");
        return typeface;
    }

    public static void overrideFontsBold(Context context, View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFontsBold(context, child);
                }
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(Bold(context));
            }
        } catch (Exception e) {
        }
    }

    public static void overrideFontsBoldItalic(Context context, View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFontsBoldItalic(context, child);
                }
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(BoldItalic(context));
            }
        } catch (Exception e) {
        }
    }

    public static void overrideFontsExtraBold(Context context, View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFontsExtraBold(context, child);
                }
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(ExtraBold(context));
            }
        } catch (Exception e) {
        }
    }

    public static void overrideFontsExtraBoldItalic(Context context, View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFontsExtraBoldItalic(context, child);
                }
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(ExtraBoldItalic(context));
            }
        } catch (Exception e) {
        }
    }

    public static void overrideFontsItalic(Context context, View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFontsItalic(context, child);
                }
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(Italic(context));
            }
        } catch (Exception e) {
        }
    }

    public static void overrideFontsLight(Context context, View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFontsLight(context, child);
                }
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(Light(context));
            }
        } catch (Exception e) {
        }
    }

    public static void overrideFontsLightItalic(Context context, View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFontsLightItalic(context, child);
                }
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(LightItalic(context));
            }
        } catch (Exception e) {
        }
    }

    public static void overrideFontsRegular(Context context, View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFontsRegular(context, child);
                }
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(Regular(context));
            }
        } catch (Exception e) {
        }
    }

    public static void overrideFontsSemibold(Context context, View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFontsSemibold(context, child);
                }
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(Semibold(context));
            }
        } catch (Exception e) {
        }
    }

    public static void overrideFontsSemiboldItalic(Context context, View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFontsSemiboldItalic(context, child);
                }
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(SemiboldItalic(context));
            }
        } catch (Exception e) {
        }
    }
}
