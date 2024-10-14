public class Course {
    public String code;
    public int capacity;
    public SLinkedList<Student>[] studentTable;
    public int size;
    public SLinkedList<Student> waitlist;

    public Course(String code) {
        this.code = code;
        this.studentTable = new SLinkedList[10];
        this.size = 0;
        this.waitlist = new SLinkedList<Student>();
        this.capacity = 10;
    }

    public Course(String code, int capacity) {
        this.code = code;
        this.studentTable = new SLinkedList[capacity];
        this.size = 0;
        this.waitlist = new SLinkedList<>();
        this.capacity = capacity;
    }

    public void changeArrayLength(int m) {
        /**
         * Creates a new array of SLinkedList<Student>, moves all the registered students into these linked lists
         * and changes this course’s studentTable field so it references this new table instead.
         * Each student in the current table will be moved to the new table.
         */
        SLinkedList<Student>[] newarr = new SLinkedList[m]; // initialize new array
        Student[] registered_students = getRegisteredStudents();
        for (int i=0; i < registered_students.length; i++){
            int id = registered_students[i].id;
            int array_slot = id % m;
            if (newarr[array_slot] == null){
                SLinkedList<Student> studentSLinkedList = new SLinkedList<Student>();
                studentSLinkedList.addFirst(registered_students[i]);
                newarr[array_slot] = studentSLinkedList;
            }
            else{
                newarr[array_slot].addLast(registered_students[i]);
            }
        }
        this.studentTable = newarr;
        this.capacity = m; // update capacity
    }

    public boolean put(Student s) {
        int array_slot = s.id % this.capacity;
        if (s.isRegisteredOrWaitlisted(this.code) || s.courseCodes.size() >= 3 ){ //unsuccessful student register
            return false;
        }
        else if (this.size < this.capacity){  // register for the course
            if (this.studentTable[array_slot] == null){ // initialize LL if none in slot
                SLinkedList<Student> studentSLinkedList = new SLinkedList<Student>();
                studentSLinkedList.addFirst(s);
                this.studentTable[array_slot] = studentSLinkedList;
            }
            else { // add Student s to proper slot
                this.studentTable[array_slot].addLast(s);
            }
            s.addCourse(this.code); // updates student
            this.size++; // update size
        }
        else if (this.size == this.capacity) { // course is at full capacity
            if (this.waitlist.size() >= this.capacity/2) { // waitlist is at full capacity
                int m = (int) Math.floor(1.5 * this.capacity); // calculate new capacity
                changeArrayLength(m);
                int[] waitlistedIDs = getWaitlistedIDs();
                Student[] wlStudents = getWaitlistedStudents();
                for (int i = 0; i < waitlistedIDs.length; i++) { // add all ppl in the waitlist to studentTable
                    int id = waitlistedIDs[i];
                    int slot = id % m;
                    if (this.studentTable[slot] == null) { // if no LL made yet for the slot
                        SLinkedList<Student> stdLinkedList = new SLinkedList<Student>();
                        stdLinkedList.addLast(wlStudents[i]);
                        this.studentTable[slot] = stdLinkedList;
                    }
                    else {
                        this.studentTable[slot].addLast(wlStudents[i]);
                    }
                    this.size++; // update size
                    wlStudents[i].addCourse(this.code);  // update each student from waitlist
                }
                this.waitlist.clear(); // clear waitlist
                this.waitlist.addFirst(s);
            }
            else {
                this.waitlist.addLast(s);  // add to the end of waitlist
            }
        }
        return true;
    }


    public Student get(int id) {
        /**
         * Returns a Student, assuming that the student is either registered for the course or on the
         * waitlist. If there is no student in the table or waitlist that has this id, get() should return null.
         */
        int array_slot = id % this.capacity;
        if (this.studentTable[array_slot] == null){  // if the slot has no linked list, check waitlist
            for (int j=0; j < this.waitlist.size(); j++){
                Student std = this.waitlist.get(j);
                if (std.id == id){
                    return std;
                }
            }
            return null;
        }
        for (int i=0; i < this.studentTable[array_slot].size(); i++){
            Student std = this.studentTable[array_slot].get(i);
            if (std.id == id){
                return std;
            }
        }
        for (int j=0; j < this.waitlist.size(); j++){
            Student std = this.waitlist.get(j);
            if (std.id == id){
                return std;
            }
        }
        return null;
    }


    public Student remove(int id) {
        /**
         * Removes Student associated with id from course or waitlist. If id not found, return null.
         * Otherwise, return Student associated with id. If student was registered, replace with the first
         * student in the waitlist. Then, remove that student from the waitlist.
         */
        int[] registeredIDs = getRegisteredIDs();
        for (int i : registeredIDs){
            if (i == id){
                int array_slot = id % this.capacity;

                for (int j=0; j < this.studentTable[array_slot].size(); j++){ //iterating through linked list
                    Student std = this.studentTable[array_slot].get(j);
                    if (std.id == id){
                        this.studentTable[array_slot].remove(j);  // remove student from course
                        std.dropCourse(this.code); // update student

                        Student[] waitlistedStudents = getWaitlistedStudents();
                        if (this.waitlist.size() > 0){  // if there is someone in waitlist
                            put(waitlistedStudents[0]);  // put first student in waitlist in the course
                            this.waitlist.removeFirst();   // remove first student from waitlist
                        }
                        else {
                            this.size--;   // reduce size by 1 if no replacement
                        }
                        return std;
                    }
                }
            }
        }
        int[] waitlistedIDs = getWaitlistedIDs();
        Student[] waitlistedStudents = getWaitlistedStudents();
        for (int k=0; k < waitlistedIDs.length; k++){   // checking waitlist if no registered id found
            if (waitlistedIDs[k] == id){
                this.waitlist.remove(k); // remove student from waitlist
                Student std = waitlistedStudents[k];
                return std;
            }
        }
        return null;
    }

    public int getCourseSize() {
        int course_size = this.size;
        return course_size;
        }

    public int[] getRegisteredIDs() {
        int course_size = getCourseSize();
        int[] registeredIDs = new int[course_size];
        int counter = 0;
        for (int i=0; i < this.capacity; i++){
            if (this.studentTable[i] != null){
                for (int j=0; j < this.studentTable[i].size(); j++){ // iterating over linked list in slot i
                    Student std = this.studentTable[i].get(j);
                    int id = std.id;
                    registeredIDs[counter] = id; // add id to the array registeredIDs
                    counter++; // update the index of the array
                }
            }
        }
        return registeredIDs;
    }

    public Student[] getRegisteredStudents() {
        int course_size = getCourseSize();
        Student[] registeredStudents = new Student[course_size];
        int counter = 0;
        for (int i=0; i < course_size; i++){
            if (this.studentTable[i] != null){
                for (int j=0; j < this.studentTable[i].size(); j++){ // iterating over linked list in slot i
                    Student std = this.studentTable[i].get(j);
                    registeredStudents[counter] = std; // add id to the array registeredIDs
                    counter++; // update the index of the array
                }
            }
        }
        return registeredStudents;
    }

    public int[] getWaitlistedIDs() {
        int waitlist_size = this.waitlist.size();
        int[] waitlistedIDs = new int[waitlist_size];
        for (int i=0; i< waitlist_size; i++){
            Student std = this.waitlist.get(i);
            waitlistedIDs[i] = std.id;
        }
        return waitlistedIDs;
    }

    public Student[] getWaitlistedStudents() {
        int waitlist_size = this.waitlist.size();
        Student[] waitlistedStudents = new Student[waitlist_size];
        for (int i=0; i< waitlist_size; i++){
            Student std = this.waitlist.get(i);
            waitlistedStudents[i] = std;
        }
        return waitlistedStudents;
    }

    public String toString() {
        String s = "Course: "+ this.code +"\n";
        s += "--------\n";
        for (int i = 0; i < this.studentTable.length; i++) {
            s += "|"+i+"     |\n";
            s += "|  ------> ";
            SLinkedList<Student> list = this.studentTable[i];
            if (list != null) {
                for (int j = 0; j < list.size(); j++) {
                    Student student = list.get(j);
                    s +=  student.id + ": "+ student.name +" --> ";
                }
            }
            s += "\n--------\n\n";
        }

        return s;
    }
}
