package com.gmail.nossr50.skills.swords;

import com.gmail.nossr50.locale.LocaleLoader;
import com.gmail.nossr50.skills.SkillCommand;
import com.gmail.nossr50.skills.SkillType;
import com.gmail.nossr50.util.Permissions;

public class SwordsCommand extends SkillCommand {
    private String counterAttackChance;
    private String counterAttackChanceLucky;
    private String bleedLength;
    private String bleedChance;
    private String bleedChanceLucky;
    private String serratedStrikesLength;
    private String serratedStrikesLengthEndurance;

    private boolean canCounter;
    private boolean canSerratedStrike;
    private boolean canBleed;

    public SwordsCommand() {
        super(SkillType.SWORDS);
    }

    @Override
    protected void dataCalculations() {
        //SERRATED STRIKES
        String[] serratedStrikesStrings = calculateLengthDisplayValues();
        serratedStrikesLength = serratedStrikesStrings[0];
        serratedStrikesLengthEndurance = serratedStrikesStrings[1];

        //BLEED
        if (skillValue >= Swords.bleedMaxBonusLevel) {
            bleedLength = String.valueOf(Swords.bleedMaxTicks);
        }
        else {
            bleedLength = String.valueOf(Swords.bleedBaseTicks);
        }

        String[] bleedStrings = calculateAbilityDisplayValues(Swords.bleedMaxBonusLevel, Swords.bleedMaxChance);
        bleedChance = bleedStrings[0];
        bleedChanceLucky = bleedStrings[1];

        //COUNTER ATTACK
        String[] counterAttackStrings = calculateAbilityDisplayValues(Swords.counterAttackMaxBonusLevel, Swords.counterAttackMaxChance);
        counterAttackChance = counterAttackStrings[0];
        counterAttackChanceLucky = counterAttackStrings[1];
    }

    @Override
    protected void permissionsCheck() {
        canBleed = Permissions.swordsBleed(player);
        canCounter = Permissions.counterAttack(player);
        canSerratedStrike = Permissions.serratedStrikes(player);
    }

    @Override
    protected boolean effectsHeaderPermissions() {
        return canBleed || canCounter || canSerratedStrike;
    }

    @Override
    protected void effectsDisplay() {
        luckyEffectsDisplay();

        if (canCounter) {
            player.sendMessage(LocaleLoader.getString("Effects.Template", new Object[] { LocaleLoader.getString("Swords.Effect.0"), LocaleLoader.getString("Swords.Effect.1", new Object[] {percent.format(1.0D / Swords.counterAttackModifier)} ) }));
        }

        if (canSerratedStrike) {
            player.sendMessage(LocaleLoader.getString("Effects.Template", new Object[] { LocaleLoader.getString("Swords.Effect.2"), LocaleLoader.getString("Swords.Effect.3", new Object[] {percent.format(1.0D / Swords.serratedStrikesModifier)}) }));
            player.sendMessage(LocaleLoader.getString("Effects.Template", new Object[] { LocaleLoader.getString("Swords.Effect.4"), LocaleLoader.getString("Swords.Effect.5", new Object[] {Swords.serratedStrikesBleedTicks}) }));
        }

        if (canBleed) {
            player.sendMessage(LocaleLoader.getString("Effects.Template", new Object[] { LocaleLoader.getString("Swords.Effect.6"), LocaleLoader.getString("Swords.Effect.7") }));
        }
    }

    @Override
    protected boolean statsHeaderPermissions() {
        return canBleed || canCounter || canSerratedStrike;
    }

    @Override
    protected void statsDisplay() {
        if (canCounter) {
            if (isLucky) {
                player.sendMessage(LocaleLoader.getString("Swords.Combat.Counter.Chance", new Object[] { counterAttackChance }) + LocaleLoader.getString("Perks.lucky.bonus", new Object[] { counterAttackChanceLucky }));
            }
            else {
                player.sendMessage(LocaleLoader.getString("Swords.Combat.Counter.Chance", new Object[] { counterAttackChance }));
            }
        }

        if (canBleed) {
            player.sendMessage(LocaleLoader.getString("Swords.Combat.Bleed.Length", new Object[] { bleedLength }));
            player.sendMessage(LocaleLoader.getString("Swords.Combat.Bleed.Note"));

            if (isLucky) {
                player.sendMessage(LocaleLoader.getString("Swords.Combat.Bleed.Chance", new Object[] { bleedChance }) + LocaleLoader.getString("Perks.lucky.bonus", new Object[] { bleedChanceLucky }));
            }
            else {
                player.sendMessage(LocaleLoader.getString("Swords.Combat.Bleed.Chance", new Object[] { bleedChance }));
            }
        }

        if (canSerratedStrike) {
            if (hasEndurance) {
                player.sendMessage(LocaleLoader.getString("Swords.SS.Length", new Object[] { serratedStrikesLength }) + LocaleLoader.getString("Perks.activationtime.bonus", new Object[] { serratedStrikesLengthEndurance }));
            }
            else {
                player.sendMessage(LocaleLoader.getString("Swords.SS.Length", new Object[] { serratedStrikesLength }));
            }
        }
    }
}
