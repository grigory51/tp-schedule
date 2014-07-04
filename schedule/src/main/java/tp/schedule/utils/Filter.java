package tp.schedule.utils;

public class Filter {
    private int subgroup, discipline, type;

    public Filter() {
        subgroup = -1;
        discipline = -1;
        type = -1;
    }

    public Filter(int subgroup, int discipline, int type) {
        this.subgroup = subgroup;
        this.discipline = discipline;
        this.type = type;
    }

    public void setSubgroup(int subgroup) {
        this.subgroup = subgroup - 1;
    }

    public void setDiscipline(int discipline) {
        this.discipline = discipline - 1;
    }

    public void setType(int type) {
        this.type = type - 1;
    }

    public int getSubgroup() {
        return subgroup;
    }

    public int getDiscipline() {
        return discipline;
    }

    public int getType() {
        return type;
    }
}
