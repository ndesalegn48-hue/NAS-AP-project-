package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.model.AppLanguage
import com.example.model.Category
import com.example.model.Translation
import com.example.ui.components.AdCard
import com.example.ui.components.NoticeCard
import com.example.ui.theme.GimbiCoffeeBrown
import com.example.ui.theme.GimbiGold
import com.example.ui.theme.GimbiGreenDark
import com.example.ui.theme.GimbiGreenPrimary
import com.example.ui.viewmodel.AdFilter
import com.example.ui.viewmodel.GimbiViewModel

@Composable
fun HomeScreen(
    viewModel: GimbiViewModel,
    onAdClick: (Long) -> Unit,
    onNavigateToCategories: () -> Unit,
    onNavigateToPostAd: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentLang by viewModel.currentLanguage.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val selectedFilter by viewModel.selectedFilter.collectAsStateWithLifecycle()
    val notices by viewModel.communityNotices.collectAsStateWithLifecycle()
    val filteredAds by viewModel.filteredAds.collectAsStateWithLifecycle()
    val featuredAds by viewModel.featuredAds.collectAsStateWithLifecycle()

    var showNoticesExpanded by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen_list"),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        // Hero Town Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_gimbi_hero),
                    contentDescription = "Gimbi Market Hero",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Gradient scrim
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.35f),
                                    GimbiGreenDark.copy(alpha = 0.85f)
                                )
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = GimbiGold
                    ) {
                        Text(
                            text = "MAGAALAA GIMBI • WEST WELEGA",
                            color = Color.White,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = Translation.appTagline(currentLang),
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = when (currentLang) {
                            AppLanguage.AFAAN_OROMOO -> "Daldala, qonnaa, manneen barnootaa fi tajaajiloota naannoo keetii lafa tokkotti argadhu!"
                            AppLanguage.AMHARIC -> "በአካባቢዎ ያሉ ንግዶች፣ ግብርና፣ ትምህርት ቤቶች እና አገልግሎቶችን በአንድ ቦታ ያግኙ!"
                            AppLanguage.ENGLISH -> "Local marketplace, schools, agriculture, coffee & services in Gimbi town."
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }
        }

        // Search Bar & Filter Chips
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.setSearchQuery(it) },
                    placeholder = {
                        Text(
                            text = Translation.searchPlaceholder(currentLang),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = GimbiGreenPrimary
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear search",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GimbiGreenPrimary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    ),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("search_text_field")
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Quick Filter Chips Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = selectedFilter == AdFilter.ALL && selectedCategory == null,
                        onClick = {
                            viewModel.selectFilter(AdFilter.ALL)
                            viewModel.selectCategory(null)
                        },
                        label = { Text("All / Hunda") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = GimbiGreenPrimary,
                            selectedLabelColor = Color.White
                        ),
                        modifier = Modifier.testTag("filter_chip_all")
                    )

                    FilterChip(
                        selected = selectedFilter == AdFilter.VERIFIED_ONLY,
                        onClick = {
                            viewModel.selectFilter(
                                if (selectedFilter == AdFilter.VERIFIED_ONLY) AdFilter.ALL else AdFilter.VERIFIED_ONLY
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        },
                        label = { Text(Translation.verifiedBadge(currentLang)) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = GimbiGreenPrimary,
                            selectedLabelColor = Color.White
                        ),
                        modifier = Modifier.testTag("filter_chip_verified")
                    )

                    FilterChip(
                        selected = selectedFilter == AdFilter.FEATURED_ONLY,
                        onClick = {
                            viewModel.selectFilter(
                                if (selectedFilter == AdFilter.FEATURED_ONLY) AdFilter.ALL else AdFilter.FEATURED_ONLY
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        },
                        label = { Text(Translation.featuredTitle(currentLang).take(8)) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = GimbiGreenPrimary,
                            selectedLabelColor = Color.White
                        ),
                        modifier = Modifier.testTag("filter_chip_featured")
                    )

                    FilterChip(
                        selected = selectedFilter == AdFilter.SPECIAL_OFFERS,
                        onClick = {
                            viewModel.selectFilter(
                                if (selectedFilter == AdFilter.SPECIAL_OFFERS) AdFilter.ALL else AdFilter.SPECIAL_OFFERS
                            )
                        },
                        label = { Text("Offers / Qophii") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = GimbiGreenPrimary,
                            selectedLabelColor = Color.White
                        ),
                        modifier = Modifier.testTag("filter_chip_offers")
                    )
                }
            }
        }

        // Community Notice Board Banner
        if (notices.isNotEmpty()) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showNoticesExpanded = !showNoticesExpanded }
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Campaign,
                                        contentDescription = "Notice",
                                        tint = GimbiGold,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = Translation.noticeBoardTitle(currentLang),
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF4E342E)
                                    )
                                }

                                Text(
                                    text = if (showNoticesExpanded) "Hide" else "View all (${notices.size})",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = GimbiGreenPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            if (!showNoticesExpanded) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = notices.first().getTitle(currentLang),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF5D4037),
                                    maxLines = 1
                                )
                            }
                        }
                    }

                    AnimatedVisibility(visible = showNoticesExpanded) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            notices.forEach { notice ->
                                NoticeCard(notice = notice, currentLanguage = currentLang)
                            }
                        }
                    }
                }
            }
        }

        // Horizontal Category Quick Bar
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = Translation.tabCategories(currentLang),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    TextButton(onClick = onNavigateToCategories) {
                        Text(
                            text = "See all / Hunda",
                            color = GimbiGreenPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(Category.values()) { cat ->
                        val isSelected = selectedCategory == cat
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) GimbiGreenPrimary else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .clickable {
                                    viewModel.selectCategory(if (isSelected) null else cat)
                                }
                                .testTag("cat_chip_${cat.id}")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (isSelected) Color.White.copy(alpha = 0.25f)
                                            else GimbiGreenPrimary.copy(alpha = 0.15f)
                                        )
                                ) {
                                    Text(
                                        text = cat.getLocalizedName(currentLang).take(1),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = if (isSelected) Color.White else GimbiGreenPrimary
                                    )
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = cat.getLocalizedName(currentLang),
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        // Section Title: Ads list
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (selectedCategory != null) selectedCategory!!.getLocalizedName(currentLang)
                        else Translation.allAdsTitle(currentLang),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${filteredAds.size} listings found in Gimbi",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = GimbiGreenPrimary,
                    modifier = Modifier.clickable { onNavigateToPostAd() }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "+ Post Ad",
                            color = Color.White,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Listings List
        if (filteredAds.isEmpty()) {
            item {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 48.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "No results",
                            tint = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No advertisements found",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Try clearing your search query or filter",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(filteredAds, key = { it.id }) { ad ->
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                    AdCard(
                        ad = ad,
                        currentLanguage = currentLang,
                        onClick = {
                            viewModel.trackView(ad.id)
                            viewModel.trackProfileVisit(ad.id)
                            onAdClick(ad.id)
                        },
                        onCallClick = { viewModel.trackCall(ad.id) },
                        onWhatsappClick = { viewModel.trackWhatsapp(ad.id) },
                        onSaveToggle = { viewModel.toggleSave(ad.id, ad.isSaved) }
                    )
                }
            }
        }
    }
}
