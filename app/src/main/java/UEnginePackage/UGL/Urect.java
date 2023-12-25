package UEnginePackage.UGL;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.microedition.khronos.opengles.GL10;


public class Urect {
    public boolean DrawChilds;
    protected double alpha;
    private List<Urect> childrens;
    public int color;
    protected double height;
    public Paint paint;
    private Urect parent;
    Rect rect;
    protected double rotate;
    public double scale;
    protected double width;
    protected double x;
    protected double y;

    public double getHeight() {
        return this.height;
    }

    public double getWidth() {
        return this.width;
    }

    public Urect(double d, double d2, double d3, double d4, int i) {
        this(d, d2, d3, d4);
        this.paint.setColor(i);
        this.color = i;
    }

    public Urect(double d, double d2, double d3, double d4) {
        this.DrawChilds = true;
        this.scale = 1.0d;
        this.childrens = Collections.synchronizedList(new ArrayList());
        this.color = -1;
        this.rect = new Rect();
        this.x = d;
        this.y = d2;
        this.height = d4;
        this.width = d3;
        this.paint = new Paint();
        setAlpha(255.0d);
        this.paint.setColor(0);
        this.color = 0;
    }

    public double getTop() {
        Urect urect = this.parent;
        return urect == null ? this.y : urect.getTop() + this.y;
    }

    public double getBottom() {
        Urect urect = this.parent;
        return (urect == null ? this.y : urect.getTop() + this.y) + Height();
    }

    public double getLeft() {
        Urect urect = this.parent;
        return urect == null ? this.x : urect.getLeft() + this.x;
    }

    public double getRight() {
        Urect urect = this.parent;
        return (urect == null ? getRelativeLeft() : urect.getLeft() + getRelativeLeft()) + Width();
    }

    public double getRelativeLeft() {
        return this.x;
    }

    public void setLeft(double d) {
        this.x = d;
    }

    public double getRelativeTop() {
        return this.y;
    }

    public void setTop(double d) {
        this.y = d;
    }

    public double getRelativeRight() {
        return this.x + Width();
    }

    public void setRight(double d) {
        setLeft(d - Width());
    }

    public double getRelativeBottom() {
        return this.y + Height();
    }

    public void setBottom(double d) {
        setTop(d - Height());
    }

    public double Height() {
        return this.height;
    }

    public void setHeight(double d) {
        this.height = d;
    }

    public double Width() {
        return this.width;
    }

    public void setWidth(double d) {
        this.width = d;
    }

    public double getRotate() {
        return this.rotate;
    }

    public void setRotate(double d) {
        this.rotate = d;
    }

    public double getAlpha() {
        Urect urect = this.parent;
        if (urect != null) {
            return (this.alpha / 255.0d) * urect.getAlpha();
        }
        return this.alpha;
    }

    public double getRelativeAlpha() {
        return this.alpha;
    }

    public void setAlpha(double d) {
        this.alpha = d;
    }

    public int getColor() {
        return this.paint.getColor();
    }

    public void setColor(int i) {
        this.paint.setColor(i);
        this.color = i;
    }

    public List<Urect> getChildrens() {
        return this.childrens;
    }

    public boolean removeParent() {
        if (this.parent != null) {
            this.parent = null;
            return true;
        }
        return false;
    }

    public void AddChild(Urect urect) {
        if (urect == null) {
            return;
        }
        if (this.childrens == null) {
            this.childrens = new ArrayList();
        }
        this.childrens.add(urect);
        urect.parent = this;
    }

    public boolean DeleteChild(Urect urect) {
        List<Urect> list = this.childrens;
        if (list != null) {
            list.remove(urect);
        }
        return urect.removeParent();
    }

    public Urect getParent() {
        return this.parent;
    }

    public Rect GetRect() {
        this.rect.left = (int) getLeft();
        this.rect.top = (int) getTop();
        this.rect.right = (int) getRight();
        this.rect.bottom = (int) getBottom();
        return this.rect;
    }

    public double getCenterX() {
        return (getLeft() + getRight()) / 2.0d;
    }

    public double getCenterY() {
        return (getTop() + getBottom()) / 2.0d;
    }

    public void Draw(GL10 gl10, float f) {
        try {
            drawChildrens(gl10, f);
        } catch (Exception e) {
            Log.e("error drw", "on Urect " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void Draw(Canvas canvas) {
        try {
            int save = canvas.save();
            canvas.rotate((int) getRotate(), (int) getCenterX(), (int) getCenterY());
            double d = this.scale;
            canvas.scale((float) d, (float) d, (int) getCenterX(), (int) getCenterY());
            this.paint.setAlpha((int) getAlpha());
            this.paint.setAntiAlias(true);
            if (this.color != 0) {
                canvas.drawRect(GetRect(), this.paint);
            }
            canvas.restoreToCount(save);
            drawChildrens(canvas);
        } catch (Exception e) {
            Log.e("error drw", "on Urect " + e.getMessage());
            e.printStackTrace();
        }
    }

    protected void drawChildrens(GL10 gl10, float f) {
        List<Urect> list;
        if (!this.DrawChilds || (list = this.childrens) == null) {
            return;
        }
        try {
            synchronized (list) {
                for (int i = 0; i < this.childrens.size(); i++) {
                    if (this.childrens.get(i) != null) {
                        this.childrens.get(i).Draw(gl10, f);
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    protected void drawChildrens(Canvas canvas) {
        List<Urect> list;
        if (!this.DrawChilds || (list = this.childrens) == null) {
            return;
        }
        try {
            synchronized (list) {
                for (int i = 0; i < this.childrens.size(); i++) {
                    if (this.childrens.get(i) != null) {
                        this.childrens.get(i).Draw(canvas);
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    public void scale(double d) {
        this.scale = d;
    }

    public boolean isCollide(Urect urect) {
        if ((urect.getLeft() <= getLeft() || urect.getLeft() > getRight()) && (urect.getRight() <= getLeft() || urect.getRight() >= getRight())) {
            return false;
        }
        if (urect.getTop() <= getTop() || urect.getTop() > getBottom()) {
            return urect.getBottom() > getTop() && urect.getBottom() < getBottom();
        }
        return true;
    }

    public void Delete() {
        Urect urect = this.parent;
        if (urect != null) {
            urect.DeleteChild(this);
        }
    }

    public void clearChilds() {
        List<Urect> list = this.childrens;
        if (list == null) {
            return;
        }
        synchronized (list) {
            for (int i = 0; i < this.childrens.size(); i++) {
                this.childrens.get(i).parent = null;
            }
        }
        this.childrens.clear();
    }
}
