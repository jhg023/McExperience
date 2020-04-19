package com.github.jhg023.spigot.skill.utility;

import com.github.jhg023.spigot.skill.Skill;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;

/**
 * A utility class that provides skill-related convenience methods.
 *
 * @author Jacob Glickman
 * @version January 5, 2020
 */
public final class SkillUtility {

    /**
     * The metadata key to use when storing skill-related values.
     */
    public static final String SKILL_METADATA_KEY = "com.github.jhg023.spigot.skill";

    /**
     * A global {@link NumberFormat} instance used to format experience values.
     */
    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getInstance(Locale.US);

    /**
     * Hard-coded values of experience mapped to their respective level (index in the array, plus one).
     */
    private static final int[] EXPERIENCE_FOR_LEVEL = {
        0, 83, 174, 276, 388, 512, 650, 801, 969, 1_154, 1_358, 1_584, 1_833, 2_107, 2_411, 2_746, 3_115, 3_523, 3_973,
        4_470, 5_018, 5_624, 6_291, 7_028, 7_842, 8_740, 9_730, 10_824, 12_031, 13_363, 14_833, 16_456, 18_247, 20_224,
        22_406, 24_815, 27_473, 30_408, 33_648, 37_224, 41_171, 45_529, 50_339, 55_649, 61_512, 67_983, 75_127, 83_014,
        91_721, 101_333, 111_945, 123_660, 136_594, 150_872, 166_636, 184_040, 203_254, 224_466, 247_886, 273_742,
        302_288, 333_804, 368_599, 407_015, 449_428, 496_254, 547_953, 605_032, 668_051, 737_627, 814_445, 899_257,
        992_895, 1_096_278, 1_210_421, 1_336_443, 1_475_581, 1_629_200, 1_798_808, 1_986_068, 2_192_818, 2_421_087,
        2_673_114, 2_951_373, 3_258_594, 3_597_792, 3_972_294, 4_385_776, 4_842_295, 5_346_332, 5_902_831, 6_517_253,
        7_195_629, 7_944_614, 8_771_558, 9_684_577, 10_692_629, 11_805_606, 13_034_431, 14_391_160
    };

    /**
     * A {@code private} constructor to prevent instantiation.
     *
     * @throws UnsupportedOperationException if invoked.
     */
    private SkillUtility() {
        throw new UnsupportedOperationException("This class cannot be instantiated!");
    }

    /**
     * Formats the tracker display to show level, current experience, and goal experience (to level up).
     *
     * @param skill the skill to format the display for.
     * @param currentExperience the current experience to use in the display.
     * @return a formatted tracker display.
     */
    public static String formatDisplay(Skill skill, int currentExperience) {
        var level = getLevelForExperience(currentExperience);

        if (level == EXPERIENCE_FOR_LEVEL.length) {
            return String.format(skill.getMaxLevelFormattedDisplay(), level, NUMBER_FORMAT.format(currentExperience));
        }

        return String.format(skill.getFormattedDisplay(), level, EXPERIENCE_FOR_LEVEL.length,
            NUMBER_FORMAT.format(currentExperience),
            NUMBER_FORMAT.format(EXPERIENCE_FOR_LEVEL[Math.min(level, EXPERIENCE_FOR_LEVEL.length - 1)]));
    }

    /**
     * Return the indefinite article, either {@code "a"} or {@code "an"}, for the specified {@link String}.
     *
     * @param s the {@link String} to determine the indefinite article for.
     * @return {@code "an"} if the specified {@link String} begins with a vowel, otherwise {@code "a"}.
     */
    public static String getIndefiniteArticle(String s) {
        switch (Character.toLowerCase(s.charAt(0))) {
            case 'a':
            case 'e':
            case 'i':
            case 'o':
            case 'u':
                return "an";
            default:
                return "a";
        }
    }

    /**
     * Gets the floor level for a specified amount of experience.
     *
     * @param experience the amount of experience.
     * @return the level that correlates to the specified experience.
     */
    public static int getLevelForExperience(int experience) {
        return Math.abs(Arrays.binarySearch(EXPERIENCE_FOR_LEVEL, experience) + 1);
    }

    /**
     * Given a specified amount of experience and a goal level, this method gets the ratio of completeness towards
     * that goal as a {@code double} ranging from {@code 0D} to {@code 1D}.
     * <br><br>
     * If the specified level is out of range, then {@code 1D} is returned.
     *
     * @param experience the specified amount of experience.
     * @param level      the specified goal level.
     * @return the progress towards the specified level given some amount of experience.
     */
    public static double getProgress(int experience, int level) {
        if (level <= 0 || level >= EXPERIENCE_FOR_LEVEL.length) {
            return 1D;
        }

        var low = EXPERIENCE_FOR_LEVEL[level - 1];
        var high = EXPERIENCE_FOR_LEVEL[level];

        return Math.min(1D, Math.max(0D, (double) (experience - low) / (high - low)));
    }
}
