import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public class BVH implements Iterable<Circle>{
    Box boundingBox;

    BVH child1;
    BVH child2;

    Circle containedCircle;

    public BVH(ArrayList<Circle> circles) {
        this.boundingBox = buildTightBoundingBox(circles);
        if (circles.size() > 1){
            ArrayList<Circle>[] partitions = split(circles, this.boundingBox);
            this.child1 = new BVH(partitions[0]);
            this.child2 = new BVH(partitions[1]);
        }
        if (circles.size() == 1){
            this.containedCircle = circles.get(0);
        }
    }

    public void draw(Graphics2D g2) {
        this.boundingBox.draw(g2);
        if (this.child1 != null) {
            this.child1.draw(g2);
        }
        if (this.child2 != null) {
            this.child2.draw(g2);
        }
    }

    public static ArrayList<Circle>[] split(ArrayList<Circle> circles, Box boundingBox) {
        double midpoint;
        ArrayList<Circle> child1= new ArrayList<Circle>();
        ArrayList<Circle> child2 = new ArrayList<Circle>();
        if (boundingBox.getWidth() >= boundingBox.getHeight()) {
            midpoint = boundingBox.getMidX();
            for (Circle c : circles) {
                if (c.position.x <= midpoint) {
                    child1.add(c);
                } else {
                    child2.add(c);
                }
            }
        }
        else{
            midpoint = boundingBox.getMidY();
            for (Circle c2 : circles){
                if (c2.position.y <= midpoint){
                    child1.add(c2);
                }
                else {
                    child2.add(c2);
                }
            }
        }
        ArrayList<Circle>[] partitions = new ArrayList[]{child1, child2};
        return partitions;
    }

    // returns the smallest possible box which fully encloses every circle in circles
    public static Box buildTightBoundingBox(ArrayList<Circle> circles) {
        Vector2 bottomLeft = new Vector2(Float.POSITIVE_INFINITY);
        Vector2 topRight = new Vector2(Float.NEGATIVE_INFINITY);

        for (Circle c : circles) {
            bottomLeft = Vector2.min(bottomLeft, c.getBoundingBox().bottomLeft);
            topRight = Vector2.max(topRight, c.getBoundingBox().topRight);
        }

        return new Box(bottomLeft, topRight);
    }

    // METHODS BELOW RELATED TO ITERATOR

    @Override
    public Iterator<Circle> iterator() {
        return new BVHIterator(this);
    }

    public class BVHIterator implements Iterator<Circle> {

        ArrayList<Circle> circles;
        Circle cur;
        int counter = 0;
        int size;

        public BVHIterator(BVH bvh) {
            circles = find_circles(bvh);
            size = circles.size();
            cur = circles.get(counter);
        }

        @Override
        public boolean hasNext() {
            return (counter != size);
        }

        @Override
        public Circle next() {
            Circle element = cur;
            counter += 1;
            if (counter != size){
                cur = circles.get(counter);
            }
            return element;
        }

        public ArrayList<Circle> find_circles(BVH bvh){
            ArrayList<Circle> circles = new ArrayList<>();

            if (bvh.containedCircle != null){
                circles.add(bvh.containedCircle);
            }
            else if (bvh.containedCircle == null && bvh.child1 != null && bvh.child2 != null){
                circles.addAll(find_circles(bvh.child1));
                circles.addAll(find_circles((bvh.child2)));

            }
            return circles;
        }
    }
}
