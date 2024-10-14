import java.util.ArrayList;
import java.util.HashSet;

public class ContactFinder {
    // Returns a HashSet of ContactResult objects representing all the contacts between circles in the scene.
    // The runtime of this method should be O(n^2) where n is the number of circles.
    public static HashSet<ContactResult> getContactsNaive(ArrayList<Circle> circles) {
        HashSet<ContactResult> contactResults = new HashSet<>();
        for (int i = 0; i < circles.size(); i++){
            for (int j = 0; j < circles.size(); j++){
                if (circles.get(i).id != circles.get(j).id) {
                    ContactResult contactResult = circles.get(i).isContacting(circles.get(j));
                    if (contactResult != null){
                        contactResults.add(contactResult);
                    }
                }
            }
        }
        return contactResults;
    }

    // Returns a HashSet of ContactResult objects representing all the contacts between circles in the scene.
    // The runtime of this method should be O(n*log(n)) where n is the number of circles.
    public static HashSet<ContactResult> getContactsBVH(ArrayList<Circle> circles, BVH bvh) {
        HashSet<ContactResult> total = new HashSet<>();
        for (Circle c : circles){
            HashSet<ContactResult> single = getContactBVH(c, bvh);
            total.addAll(single);
        }
        return total;
    }

    // Takes a single circle c and a BVH bvh.
    // Returns a HashSet of ContactResult objects representing contacts between c
    // and the circles contained in the leaves of the bvh.
    public static HashSet<ContactResult> getContactBVH(Circle c, BVH bvh) {
        HashSet<ContactResult> contactResults = new HashSet<>();
        if (!c.getBoundingBox().intersectBox(bvh.boundingBox)){
            return contactResults;
        }
        else {
            // if bvh is a leaf node and the circle does not have the same id
            if (bvh.containedCircle != null && bvh.containedCircle.id != c.id){
                ContactResult contactResult = c.isContacting(bvh.containedCircle);
                if (contactResult != null){
                    contactResults.add(contactResult);
                }
            }
            // if bvh is not a leaf node, and children are non-null
            else if (bvh.containedCircle == null && bvh.child1 != null && bvh.child2 != null){
                contactResults.addAll(getContactBVH(c, bvh.child1));
                contactResults.addAll(getContactBVH(c, bvh.child2));
            }
        }
        return contactResults;
    }
}
