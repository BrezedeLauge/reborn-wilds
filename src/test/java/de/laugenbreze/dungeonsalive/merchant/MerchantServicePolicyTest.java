package de.laugenbreze.dungeonsalive.merchant;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MerchantServicePolicyTest {
    private static MerchantServicePolicy.Input input(int count, int damage, int maxDamage, int rank, int enchantment, int emeralds, boolean collected) {
        return new MerchantServicePolicy.Input(count, maxDamage > 0, damage, maxDamage, true, rank, true, enchantment, emeralds, collected);
    }
    @Test void repairPriceDependsOnActualDamageAndNeverExceedsOneEmeraldStack() {
        assertEquals(new MerchantServicePolicy.Quote(ServiceStatus.READY, 3), MerchantServicePolicy.quote(ServiceKind.BLACKSMITH, input(1, 1, 100, 0, 0, 3, false)));
        assertEquals(new MerchantServicePolicy.Quote(ServiceStatus.NEED_PAYMENT, 7), MerchantServicePolicy.quote(ServiceKind.BLACKSMITH, input(1, 50, 100, 0, 0, 6, false)));
        assertEquals(new MerchantServicePolicy.Quote(ServiceStatus.READY, 12), MerchantServicePolicy.quote(ServiceKind.BLACKSMITH, input(1, Integer.MAX_VALUE, 100, 0, 0, 12, false)));
        assertEquals(ServiceStatus.MAXIMUM, MerchantServicePolicy.quote(ServiceKind.BLACKSMITH, input(1, 0, 100, 0, 0, 64, false)).status());
    }
    @Test void upgradeRanksHaveExplicitIncreasingCostsAndStopAfterThree() {
        for (int rank = 0; rank < 3; rank++) {
            int price = 16 * (rank + 1);
            assertEquals(new MerchantServicePolicy.Quote(ServiceStatus.NEED_PAYMENT, price), MerchantServicePolicy.quote(ServiceKind.POWERSMITH, input(1, 0, 100, rank, 0, price - 1, false)));
            assertEquals(new MerchantServicePolicy.Quote(ServiceStatus.READY, price), MerchantServicePolicy.quote(ServiceKind.POWERSMITH, input(1, 0, 100, rank, 0, price, false)));
        }
        assertEquals(ServiceStatus.MAXIMUM, MerchantServicePolicy.quote(ServiceKind.POWERSMITH, input(1, 0, 100, 3, 0, 64, false)).status());
        assertEquals(1.15, MerchantServicePolicy.damageMultiplier(999), .000001);
        assertEquals(1, MerchantServicePolicy.damageMultiplier(-2), .000001);
    }
    @Test void enchantmentsPriceTheirNextLevelAndDoNotOfferLevelFour() {
        for (int level = 0; level < 3; level++) assertEquals(new MerchantServicePolicy.Quote(ServiceStatus.READY, 8 * (level + 1)),
                MerchantServicePolicy.quote(ServiceKind.ENCHANTSMITH, input(1, 0, 100, 0, level, 64, false)));
        assertEquals(ServiceStatus.MAXIMUM, MerchantServicePolicy.quote(ServiceKind.ENCHANTSMITH, input(1, 0, 100, 0, 3, 64, false)).status());
        var unsupported = new MerchantServicePolicy.Input(1, true, 10, 100, false, 0, false, 0, 64, false);
        assertEquals(ServiceStatus.UNSUPPORTED, MerchantServicePolicy.quote(ServiceKind.ENCHANTSMITH, unsupported).status());
    }
    @Test void aPaidResultMustBeCollectedAndAStackCannotBeConvertedAsASingleItem() {
        for (var service : ServiceKind.values()) {
            assertEquals(ServiceStatus.COLLECT_RESULT, MerchantServicePolicy.quote(service, input(1, 50, 100, 0, 0, 64, true)).status());
            assertEquals(ServiceStatus.UNSUPPORTED, MerchantServicePolicy.quote(service, input(2, 50, 100, 0, 0, 64, false)).status());
            assertEquals(ServiceStatus.EMPTY, MerchantServicePolicy.quote(service, input(0, 0, 0, 0, 0, 64, true)).status());
        }
    }
}
