package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.AdvertisementEntity
import com.example.model.AppLanguage
import com.example.model.Category
import com.example.ui.theme.GimbiCoffeeBrown
import com.example.ui.theme.GimbiGreenContainer
import com.example.ui.theme.GimbiGreenDark
import com.example.ui.theme.GimbiGreenPrimary
import com.example.ui.theme.VerifiedBlue

/**
 * A dedicated, reusable LazyColumn UI component that displays a list of advertisement cards,
 * each showing an image, title, and category tag.
 */
@Composable
fun AdvertisementCardsList(
    ads: List<AdvertisementEntity>,
    currentLanguage: AppLanguage = AppLanguage.AFAAN_OROMOO,
    onAdClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
    headerContent: (@Composable () -> Unit)? = null,
    emptyContent: (@Composable () -> Unit)? = null
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("advertisement_cards_lazy_column"),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        if (headerContent != null) {
            item(key = "header_item") {
                headerContent()
            }
        }

        if (ads.isEmpty()) {
            item(key = "empty_state_item") {
                if (emptyContent != null) {
                    emptyContent()
                } else {
                    DefaultEmptyAdvertisements()
                }
            }
        } else {
            items(
                items = ads,
                key = { it.id }
            ) { ad ->
                AdvertisementCardItem(
                    ad = ad,
                    currentLanguage = currentLanguage,
                    onClick = { onAdClick(ad.id) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

/**
 * Individual advertisement card showing:
 * 1. Image (hero banner / thumbnail)
 * 2. Category tag (stylish pill badge with category name)
 * 3. Title (bold, readable typography)
 * 4. Contextual metadata: business name, location, and price
 */
@Composable
fun AdvertisementCardItem(
    ad: AdvertisementEntity,
    currentLanguage: AppLanguage,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val category = Category.values().find { it.id == ad.categoryId } ?: Category.BUSINESS

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (ad.isFeatured) 4.dp else 2.dp
        ),
        modifier = modifier
            .testTag("ad_card_item_${ad.id}")
            .clickable { onClick() }
    ) {
        Column {
            // 1. IMAGE SECTION WITH CATEGORY TAG OVERLAY
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            ) {
                when (ad.imageResName) {
                    "img_coffee_agri" -> {
                        Image(
                            painter = painterResource(id = R.drawable.img_coffee_agri),
                            contentDescription = ad.getTitle(currentLanguage),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .testTag("ad_image_${ad.id}")
                        )
                    }
                    "img_gimbi_hero" -> {
                        Image(
                            painter = painterResource(id = R.drawable.img_gimbi_hero),
                            contentDescription = ad.getTitle(currentLanguage),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .testTag("ad_image_${ad.id}")
                        )
                    }
                    else -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(GimbiGreenDark, GimbiCoffeeBrown)
                                    )
                                )
                                .testTag("ad_placeholder_image_${ad.id}"),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Surface(
                                    shape = CircleShape,
                                    color = Color.White.copy(alpha = 0.2f),
                                    modifier = Modifier.size(50.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = category.getLocalizedName(currentLanguage).take(2).uppercase(),
                                            color = Color.White,
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 20.sp
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = category.getLocalizedName(currentLanguage),
                                    color = Color.White.copy(alpha = 0.85f),
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        }
                    }
                }

                // 2. CATEGORY TAG (Prominent badge on top-start of image)
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color.Black.copy(alpha = 0.72f),
                    modifier = Modifier
                        .padding(10.dp)
                        .align(Alignment.TopStart)
                        .testTag("ad_category_tag_${ad.id}")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(GimbiGreenContainer)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = category.getLocalizedName(currentLanguage),
                            color = Color.White,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // Price Tag (top-end overlay if present)
                if (ad.priceText.isNotBlank()) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = GimbiGreenPrimary,
                        modifier = Modifier
                            .padding(10.dp)
                            .align(Alignment.TopEnd)
                    ) {
                        Text(
                            text = ad.priceText,
                            color = Color.White,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // 3. TITLE & DETAILS SECTION
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp)
            ) {
                // Business name + verification indicator
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = ad.businessName,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    if (ad.isVerified) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Verified",
                            tint = VerifiedBlue,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Title (prominent)
                Text(
                    text = ad.getTitle(currentLanguage),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.testTag("ad_title_${ad.id}")
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Location info
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${ad.locationKebele}, Gimbi",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun DefaultEmptyAdvertisements(modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.SearchOff,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "No advertisements available",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Check back soon or post a new advertisement to this category.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
