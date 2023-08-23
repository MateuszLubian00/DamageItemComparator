package io.github.mateuszlubian00.itemcompare.model;

public class Formulas {

    /** Helper class to describe single formula. */
    public class formula {
        String text;
        String[] requirements;

        public formula() {
            text = "";
            requirements = new String[]{};
        }
        public formula(String text, String[] requirements){
            this.text = text;
            this.requirements = requirements;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String[] getRequirements() {
            return requirements;
        }

        public void setRequirements(String[] requirements) {
            this.requirements = requirements;
        }

        @Override
        public String toString() {
            return String.format(text, (Object[]) requirements);
        }
    }
    public formula totalAttack;
    public formula totalAttackSpeed;
    public formula totalCritChance;
    public formula totalCritDamage;
    public formula totalDefense;
    public formula defenseEffect;
    public formula totalHP;

    public Formulas() {
        totalAttack = new formula("%s + %s", new String[]{"atk", "i_atk"});
        totalAttackSpeed = new formula("%s + %s", new String[]{"atkspd", "i_atkspd"});
        totalCritChance = new formula("%s + %s", new String[]{"crit", "i_crit"});
        totalCritDamage = new formula("%s", new String[]{"2.25"});
        totalDefense = new formula("%s + %s", new String[]{"def", "i_def"});
        defenseEffect = new formula("%s / ( %s + %s )", new String[]{"100", "100", "t_def"});
        totalHP = new formula("%s + %s", new String[]{"hp", "i_hp"});
    }


}
