package com.example.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.PackageTier
import com.example.ui.theme.GimbiGold
import com.example.ui.theme.GimbiGreenPrimary

@Composable
fun PackageSelector(
    selectedPackage: PackageTier,
    currentLanguage: AppLanguage,
    onSelectPackage: (PackageTier) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        PackageTier.values().forEach { tier ->
            val isSelected = tier == selectedPackage
            val (badgeText, perks) = when (tier) {
                PackageTier.FREE -> Pair(
                    "Community",
                    when (currentLanguage) {
                        AppLanguage.AFAAN_OROMOO -> listOf("Galmee daldalaa bu'uuraa", "Guyyoota 14 qofa tura", "Tarreeffama hawaasaa")
                        AppLanguage.AMHARIC -> listOf("መሰረታዊ የንግድ ምዝገባ", "ለ14 ቀናት ብቻ የሚቆይ", "በማህበረሰብ ማውጫ ውስጥ")
                        AppLanguage.ENGLISH -> listOf("Basic business listing", "14 days active duration", "Community directory search")
                    }
                )
                PackageTier.STANDARD -> Pair(
                    "Recommended",
                    when (currentLanguage) {
                        AppLanguage.AFAAN_OROMOO -> listOf("Gara mata-duree ramaddiitti mul'ata", "Guyyoota 30 guutuu", "Mallattoo mirkaneessaa qabaata", "Lakkoofsa bilbilaa kallattiin")
                        AppLanguage.AMHARIC -> listOf("በምድቦች አናት ላይ ይታያል", "ለሙሉ 30 ቀናት የሚቆይ", "የማረጋገጫ ባጅ ክለሳ", "ቀጥታ የስልክ እና አድራሻ ጥሪ")
                        AppLanguage.ENGLISH -> listOf("Top of category placement", "Active for 30 full days", "Verification badge review", "Direct phone & call leads")
                    }
                )
                PackageTier.PREMIUM -> Pair(
                    "Best Results",
                    when (currentLanguage) {
                        AppLanguage.AFAAN_OROMOO -> listOf("Fuula dura irratti beeksisa filatamaa", "Guyyoota 60 tura", "Daashboordii bu'aa fi lakkoofsa", "Gara WhatsApp kallattiin geessa", "Asxaa mirkanaa'e")
                        AppLanguage.AMHARIC -> listOf("በዋናው ገፅ ላይ ተለይቶ የሚቀርብ", "ለ60 ቀናት የሚቆይ", "የተሟላ የአፈጻጸም ዳሽቦርድ", "ቀጥታ የዋትስአፕ ደንበኛ ማግኛ", "የተረጋገጠ ሰማያዊ ባጅ")
                        AppLanguage.ENGLISH -> listOf("Featured Carousel on Home page", "Active 60 full days", "Full analytics & views dashboard", "Instant WhatsApp lead generation", "Priority verification badge")
                    }
                )
            }

            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                    else MaterialTheme.colorScheme.surface
                ),
                border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, GimbiGreenPrimary)
                else androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("package_${tier.name}")
                    .clickable { onSelectPackage(tier) }
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = if (isSelected) GimbiGreenPrimary else Color.Transparent,
                                modifier = Modifier
                                    .size(22.dp)
                                    .border(
                                        2.dp,
                                        if (isSelected) GimbiGreenPrimary else MaterialTheme.colorScheme.outline,
                                        CircleShape
                                    )
                            ) {
                                if (isSelected) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Selected",
                                            tint = Color.White,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Text(
                                text = tier.getLocalizedName(currentLanguage),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) GimbiGreenPrimary else MaterialTheme.colorScheme.onSurface
                            )
                        }

                        if (tier == PackageTier.PREMIUM) {
                            Surface(
                                color = GimbiGold,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = "Premium",
                                        tint = Color.White,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text(
                                        text = badgeText,
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    perks.forEach { perk ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = GimbiGreenPrimary,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = perk,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}
