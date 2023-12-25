package UEnginePackage.UGL;


public class Transition {
    public static double easeBounceOut(double d, double d2, double d3, double d4) {
        double d5;
        double d9;
        double d52;
        double d8 = d / d4;
        if (d8 < 0.36363636363636365d) {
            d52 = 7.5625d * d8 * d8;
        } else {
            if (d8 < 0.7272727272727273d) {
                double d92 = d8 - 0.5454545454545454d;
                d5 = 7.5625d * d92 * d92;
                d9 = 0.75d;
            } else if (d8 < 0.9090909090909091d) {
                double d10 = d8 - 0.8181818181818182d;
                d5 = 7.5625d * d10 * d10;
                d9 = 0.9375d;
            } else {
                double d11 = d8 - 0.9545454545454546d;
                d5 = 7.5625d * d11 * d11;
                d9 = 0.984375d;
            }
            d52 = d5 + d9;
        }
        double d6 = d3 * d52;
        return d6 + d2;
    }

    public static double easeInCubic(double d, double d2, double d3, double d4) {
        double d5 = d / d4;
        return (d3 * d5 * d5 * d5) + d2;
    }

    public static double easeInOutCubic(double d, double d2, double d3, double d4) {
        double d5;
        double d6 = d / (d4 / 2.0d);
        if (d6 < 1.0d) {
            d5 = (d3 / 2.0d) * d6 * d6 * d6;
        } else {
            double d7 = d6 - 2.0d;
            d5 = (d3 / 2.0d) * ((d7 * d7 * d7) + 2.0d);
        }
        return d5 + d2;
    }

    public static double easeInOutQuad(double d, double d2, double d3, double d4) {
        double d5;
        double d6 = d / (d4 / 2.0d);
        if (d6 < 1.0d) {
            d5 = (d3 / 2.0d) * d6;
        } else {
            double d7 = d6 - 1.0d;
            double d52 = (-d3) / 2.0d;
            d6 = ((d7 - 2.0d) * d7) - 1.0d;
            d5 = d52;
        }
        return (d5 * d6) + d2;
    }

    public static double easeInOutQuart(double d, double d2, double d3, double d4) {
        double d5;
        double d6 = d / (d4 / 2.0d);
        if (d6 < 1.0d) {
            d5 = (d3 / 2.0d) * d6 * d6 * d6 * d6;
        } else {
            double d7 = d6 - 2.0d;
            d5 = ((-d3) / 2.0d) * ((((d7 * d7) * d7) * d7) - 2.0d);
        }
        return d5 + d2;
    }

    public static double easeInOutQudouble(double d, double d2, double d3, double d4) {
        double d5;
        double d6 = d / (d4 / 2.0d);
        if (d6 < 1.0d) {
            d5 = (d3 / 2.0d) * d6 * d6 * d6 * d6 * d6;
        } else {
            double d7 = d6 - 2.0d;
            d5 = (d3 / 2.0d) * ((d7 * d7 * d7 * d7 * d7) + 2.0d);
        }
        return d5 + d2;
    }

    public static double easeInOutQuint(double d, double d2, double d3, double d4) {
        double d5;
        double d6 = d / (d4 / 2.0d);
        if (d6 < 1.0d) {
            d5 = (d3 / 2.0d) * d6 * d6 * d6 * d6 * d6;
        } else {
            double d7 = d6 - 2.0d;
            d5 = (d3 / 2.0d) * ((d7 * d7 * d7 * d7 * d7) + 2.0d);
        }
        return d5 + d2;
    }

    public static double easeInQuad(double d, double d2, double d3, double d4) {
        double d5 = d / d4;
        return (d3 * d5 * d5) + d2;
    }

    public static double easeInQuart(double d, double d2, double d3, double d4) {
        double d5 = d / d4;
        return (d3 * d5 * d5 * d5 * d5) + d2;
    }

    public static double easeInQudouble(double d, double d2, double d3, double d4) {
        double d5 = d / d4;
        return (d3 * d5 * d5 * d5 * d5 * d5) + d2;
    }

    public static double easeInQuint(double d, double d2, double d3, double d4) {
        double d5 = d / d4;
        return (d3 * d5 * d5 * d5 * d5 * d5) + d2;
    }

    public static double easeOutCubic(double d, double d2, double d3, double d4) {
        double d5 = (d / d4) - 1.0d;
        return (((d5 * d5 * d5) + 1.0d) * d3) + d2;
    }

    public static double easeOutQuad(double d, double d2, double d3, double d4) {
        double d5 = d / d4;
        return ((-d3) * d5 * (d5 - 2.0d)) + d2;
    }

    public static double easeOutQuart(double d, double d2, double d3, double d4) {
        double d5 = (d / d4) - 1.0d;
        return ((-d3) * ((((d5 * d5) * d5) * d5) - 1.0d)) + d2;
    }

    public static double easeOutQudouble(double d, double d2, double d3, double d4) {
        double d5 = (d / d4) - 1.0d;
        return (((d5 * d5 * d5 * d5 * d5) + 1.0d) * d3) + d2;
    }

    public static double easeOutQuint(double d, double d2, double d3, double d4) {
        double d5 = (d / d4) - 1.0d;
        return (((d5 * d5 * d5 * d5 * d5) + 1.0d) * d3) + d2;
    }

    public static double linearTween(double d, double d2, double d3, double d4) {
        return ((d3 * d) / d4) + d2;
    }

    public static double GetValue(Transition_Type transition_Type, double d, double d2, double d3, double d4) {
        if (transition_Type != Transition_Type.easeInCirc) {
            if (transition_Type != Transition_Type.easeInCubic) {
                if (transition_Type != Transition_Type.easeInExpo) {
                    if (transition_Type != Transition_Type.easeInOutCirc) {
                        if (transition_Type != Transition_Type.easeInOutCubic) {
                            if (transition_Type != Transition_Type.easeInOutExpo) {
                                if (transition_Type != Transition_Type.easeInOutQuad) {
                                    if (transition_Type != Transition_Type.easeInOutQuart) {
                                        if (transition_Type != Transition_Type.easeInOutQuint) {
                                            if (transition_Type != Transition_Type.easeInOutSine) {
                                                if (transition_Type != Transition_Type.easeInQuad) {
                                                    if (transition_Type != Transition_Type.easeInQuart) {
                                                        if (transition_Type != Transition_Type.easeInQuint) {
                                                            if (transition_Type != Transition_Type.easeInSine) {
                                                                if (transition_Type != Transition_Type.easeOutCirc) {
                                                                    if (transition_Type != Transition_Type.easeOutCubic) {
                                                                        if (transition_Type != Transition_Type.easeOutExpo) {
                                                                            if (transition_Type != Transition_Type.easeOutQuad) {
                                                                                if (transition_Type != Transition_Type.easeOutQuart) {
                                                                                    if (transition_Type != Transition_Type.easeOutQuint) {
                                                                                        if (transition_Type != Transition_Type.easeOutSine) {
                                                                                            if (transition_Type != Transition_Type.linearTween) {
                                                                                                if (transition_Type != Transition_Type.Special1) {
                                                                                                    if (transition_Type != Transition_Type.Special2) {
                                                                                                        if (transition_Type != Transition_Type.Special3) {
                                                                                                            if (transition_Type != Transition_Type.Special4) {
                                                                                                                if (transition_Type == Transition_Type.easeInBoune) {
                                                                                                                    return easeBounceIn(d, d2, d3 - d2, d4);
                                                                                                                }
                                                                                                                Transition_Type transition_Type2 = Transition_Type.easeOutbounce;
                                                                                                                if (transition_Type == transition_Type2) {
                                                                                                                    return easeBounceOut(d, d2, d3 - d2, d4);
                                                                                                                }
                                                                                                                if (transition_Type == transition_Type2) {
                                                                                                                    return easeBounceInOut(d, d2, d3 - d2, d4);
                                                                                                                }
                                                                                                                return easeInOutSine(d, d2, d3 - d2, d4);
                                                                                                            }
                                                                                                            return easeOutSine(d, d2, d3 - d2, d4);
                                                                                                        }
                                                                                                        return easeOutCubic(d, d2, d3 - d2, d4);
                                                                                                    }
                                                                                                    return easeInOutQuart(d, d2, d3 - d2, d4);
                                                                                                }
                                                                                                return easeInOutQuad(d, d2, d3 - d2, d4);
                                                                                            }
                                                                                            return linearTween(d, d2, d3 - d2, d4);
                                                                                        }
                                                                                        return easeOutSine(d, d2, d3 - d2, d4);
                                                                                    }
                                                                                    return easeOutQuint(d, d2, d3 - d2, d4);
                                                                                }
                                                                                return easeOutQuart(d, d2, d3 - d2, d4);
                                                                            }
                                                                            return easeOutQuad(d, d2, d3 - d2, d4);
                                                                        }
                                                                        return easeOutExpo(d, d2, d3 - d2, d4);
                                                                    }
                                                                    return easeOutCubic(d, d2, d3 - d2, d4);
                                                                }
                                                                return easeOutCirc(d, d2, d3 - d2, d4);
                                                            }
                                                            return easeInSine(d, d2, d3 - d2, d4);
                                                        }
                                                        return easeInQuint(d, d2, d3 - d2, d4);
                                                    }
                                                    return easeInQuart(d, d2, d3 - d2, d4);
                                                }
                                                return easeInQuad(d, d2, d3 - d2, d4);
                                            }
                                            return easeInOutSine(d, d2, d3 - d2, d4);
                                        }
                                        return easeInOutQuint(d, d2, d3 - d2, d4);
                                    }
                                    return easeInOutQuart(d, d2, d3 - d2, d4);
                                }
                                return easeInOutQuad(d, d2, d3 - d2, d4);
                            }
                            return easeInOutExpo(d, d2, d3 - d2, d4);
                        }
                        return easeInOutCubic(d, d2, d3 - d2, d4);
                    }
                    return easeInOutCirc(d, d2, d3 - d2, d4);
                }
                return easeInExpo(d, d2, d3 - d2, d4);
            }
            return easeInCubic(d, d2, d3 - d2, d4);
        }
        return easeInCirc(d, d2, d3 - d2, d4);
    }

    public static double easeInSine(double d, double d2, double d3, double d4) {
        return ((-d3) * Math.cos((d / d4) * 1.5707963267948966d)) + d3 + d2;
    }

    public static double easeOutSine(double d, double d2, double d3, double d4) {
        return (Math.sin((d / d4) * 1.5707963267948966d) * d3) + d2;
    }

    public static double easeInOutSine(double d, double d2, double d3, double d4) {
        return (((-d3) / 2.0d) * (Math.cos((3.141592653589793d * d) / d4) - 1.0d)) + d2;
    }

    public static double easeInExpo(double d, double d2, double d3, double d4) {
        return (Math.pow(2.0d, ((d / d4) - 1.0d) * 10.0d) * d3) + d2;
    }

    public static double easeOutExpo(double d, double d2, double d3, double d4) {
        return (((-Math.pow(2.0d, ((-10.0d) * d) / d4)) + 1.0d) * d3) + d2;
    }

    public static double easeInOutExpo(double d, double d2, double d3, double d4) {
        double d5;
        double d6;
        double d7 = d / (d4 / 2.0d);
        if (d7 < 1.0d) {
            d5 = d3 / 2.0d;
            d6 = Math.pow(2.0d, (d7 - 1.0d) * 10.0d);
        } else {
            d5 = d3 / 2.0d;
            d6 = 2.0d + (-Math.pow(2.0d, (d7 - 1.0d) * (-10.0d)));
        }
        return (d5 * d6) + d2;
    }

    public static double easeInCirc(double d, double d2, double d3, double d4) {
        double d5 = d / d4;
        return ((-d3) * (Math.sqrt(1.0d - (d5 * d5)) - 1.0d)) + d2;
    }

    public static double easeOutCirc(double d, double d2, double d3, double d4) {
        double d5 = (d / d4) - 1.0d;
        return (Math.sqrt(1.0d - (d5 * d5)) * d3) + d2;
    }

    public static double easeInOutCirc(double d, double d2, double d3, double d4) {
        double d7;
        double d5;
        double d6 = d / (d4 / 2.0d);
        if (d6 < 1.0d) {
            d7 = (-d3) / 2.0d;
            d5 = Math.sqrt(1.0d - (d6 * d6)) - 1.0d;
        } else {
            double d52 = d6 - 2.0d;
            d7 = d3 / 2.0d;
            d5 = 1.0d + Math.sqrt(1.0d - (d52 * d52));
        }
        return (d7 * d5) + d2;
    }

    public static double easeBounceIn(double d, double d2, double d3, double d4) {
        return (d3 - easeBounceOut(d4 - d, 0.0d, d3, d4)) + d2;
    }

    public static double easeBounceInOut(double d, double d2, double d3, double d4) {
        double easeBounceOut;
        if (d < d4 / 2.0d) {
            easeBounceOut = easeBounceIn(2.0d * d, 0.0d, d3, d4) * 0.5d;
        } else {
            double easeBounceOut2 = 2.0d * d;
            easeBounceOut = (easeBounceOut(easeBounceOut2 - d4, 0.0d, d3, d4) * 0.5d) + (d3 * 0.5d);
        }
        return easeBounceOut + d2;
    }
}
