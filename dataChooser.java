class dataChooser{
    public String[][] gradesdata;
    public String[][] StudentInfo;
    public int course;
    public String feature;
    //We choose an array made of the grades for the course
    public dataChooser(String[][] data, String[][] StudentInfo, int course) {
        this.StudentInfo = StudentInfo;
        this.gradesdata = data;
        this.course = course;
    }
    //We choose an array made of the features of the students from StudentInformation
    public dataChooser(String[][] data, String[][] StudentInfo,int Course, String feature) {
        this.StudentInfo = StudentInfo;
        this.gradesdata = data;
        this.course= Course;
        this.feature = feature;
    }
    
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!
    
    //Here you do all of the process for filtering data based on the course or feature you put in
    public String[] getData(){
        return null;
    }
    
    
}

