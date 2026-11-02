package de.laugenbreze.dungeonsalive.entity.peaceful;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PeacefulProfilesTest {
    @Test void optionalNaturalCompanionsKeepTheirExistingPetRegistrationsAndBodyTypes() {
        Set<String> naturalCompanions = PeacefulProfiles.all().stream()
                .filter(profile -> profile.kind() == PeacefulProfiles.Kind.PET && profile.wildlifeEligible())
                .map(PeacefulProfiles.Profile::id).collect(Collectors.toSet());
        assertEquals(Set.of("pet_toucan", "pet_raven", "pet_red_panda", "pet_golden_parrot", "pet_hedgehog", "pet_racoon",
                "pet_royal_penguin", "pet_cluckshroom", "pet_dark_baby_goat", "pet_baby_ghast",
                "pet_zombified_baby_pig", "pet_baby_squid", "pet_ruby_baby_turtle", "pet_mole", "pet_duck"), naturalCompanions);
        assertEquals(53, PeacefulProfiles.all().size(), "Natural roles must not create extra registry profiles");
        assertEquals("fox", PeacefulProfiles.get("pet_red_panda").host());
        assertTrue(PeacefulProfiles.get("pet_baby_squid").aquatic());
        assertFalse(PeacefulProfiles.get("pet_baby_squid").flying());
        assertTrue(PeacefulProfiles.get("pet_ruby_baby_turtle").amphibious());
        assertEquals("chicken", PeacefulProfiles.get("pet_duck").host());
        assertFalse(PeacefulProfiles.get("pet_duck").aquatic());
        assertFalse(PeacefulProfiles.get("pet_duck").amphibious(),
                "Only the natural duck family receives amphibious navigation; ordinary pets keep their existing body type");
        assertFalse(PeacefulProfiles.get("pet_duck").flying());
    }

    @Test void birdsAreExplicitWhileMagicalFlyersAndUnrelatedCompanionsKeepTheirIdentity() {
        assertEquals(Set.of("pet_toucan", "pet_raven", "pet_golden_parrot"), PeacefulProfiles.all().stream()
                .filter(PeacefulProfiles.Profile::wildBirdEligible).map(PeacefulProfiles.Profile::id).collect(Collectors.toSet()));
        for (var profile : PeacefulProfiles.all()) {
            if (profile.wildBirdEligible()) assertTrue(profile.flying());
            if (profile.kind() != PeacefulProfiles.Kind.PET && profile.kind() != PeacefulProfiles.Kind.WILDLIFE)
                assertFalse(profile.wildlifeEligible(), "Civilian, merchant and key ownership must not become wildlife: " + profile.id());
        }
        assertTrue(PeacefulProfiles.get("pet_baby_ghast").flying());
        assertFalse(PeacefulProfiles.get("pet_baby_ghast").wildBirdEligible());
        assertFalse(PeacefulProfiles.get("pet_baby_wooly_cow").wildlifeEligible());
        assertFalse(PeacefulProfiles.get("summoned_guardian_vex").wildlifeEligible());
    }

    @Test void smallNightAnimalsAndDayBirdsUseOppositeSharedGroupSchedules() {
        UUID group = new UUID(1, 0);
        for (String id : new String[]{"wisp", "pet_racoon", "pet_hedgehog", "pet_mole"}) {
            var profile = PeacefulProfiles.get(id);
            assertTrue(profile.nocturnalWildlife());
            assertEquals(WildlifeGroupPolicy.Activity.REST,
                    WildlifeGroupPolicy.activity(group, 150, 6_000, profile.nocturnalWildlife(), false, Long.MIN_VALUE));
            assertEquals(WildlifeGroupPolicy.Activity.WANDER,
                    WildlifeGroupPolicy.activity(group, 150, 18_000, profile.nocturnalWildlife(), false, Long.MIN_VALUE));
        }
        for (String id : new String[]{"pet_toucan", "pet_raven", "pet_red_panda", "pet_golden_parrot", "pet_baby_ghast"}) {
            var profile = PeacefulProfiles.get(id);
            assertFalse(profile.nocturnalWildlife());
            assertEquals(WildlifeGroupPolicy.Activity.WANDER,
                    WildlifeGroupPolicy.activity(group, 150, 6_000, profile.nocturnalWildlife(), false, Long.MIN_VALUE));
            assertEquals(WildlifeGroupPolicy.Activity.REST,
                    WildlifeGroupPolicy.activity(group, 150, 18_000, profile.nocturnalWildlife(), false, Long.MIN_VALUE));
        }
    }
}
