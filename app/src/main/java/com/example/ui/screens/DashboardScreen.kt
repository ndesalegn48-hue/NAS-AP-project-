package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MarkEmailRead
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.AdvertisementEntity
import com.example.model.Inquiry
import com.example.model.Translation
import com.example.ui.theme.GimbiCoffeeBrown
import com.example.ui.theme.GimbiGold
import com.example.ui.theme.GimbiGreenContainer
import com.example.ui.theme.GimbiGreenPrimary
import com.example.ui.viewmodel.GimbiViewModel

@Composable
fun DashboardScreen(
    viewModel: GimbiViewModel,
    onNavigateToPostAd: () -> Unit,
    onAdClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val currentLang by viewModel.currentLanguage.collectAsStateWithLifecycle()
    val allAds by viewModel.filteredAds.collectAsStateWithLifecycle()
    val userCreatedAds by viewModel.userCreatedAds.collectAsStateWithLifecycle()
    val inquiries by viewModel.allInquiries.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // Aggregate analytics across ads
    val myAds = if (userCreatedAds.isNotEmpty()) userCreatedAds else allAds.take(3)
    val totalViews = myAds.sumOf { it.viewsCount }
    val totalCalls = myAds.sumOf { it.callsCount }
    val totalWhatsapp = myAds.sumOf { it.whatsappCount }
    val totalDirections = myAds.sumOf { it.directionsCount }
    val totalProfileVisits = myAds.sumOf { it.profileVisitsCount }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("dashboard_screen"),
        contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Dashboard Header
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = GimbiGreenContainer,
                        modifier = Modifier.size(38.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.BarChart,
                                contentDescription = "Analytics",
                                tint = GimbiGreenPrimary,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = Translation.tabDashboard(currentLang),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Commercial advertiser insights in Gimbi",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Button(
                    onClick = onNavigateToPostAd,
                    colors = ButtonDefaults.buttonColors(containerColor = GimbiGreenPrimary),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("+ New Ad", style = MaterialTheme.typography.labelSmall)
                }
            }
        }

        // Metrics Grid
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = Translation.dashboardMetricsTitle(currentLang),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MetricBox(
                        title = Translation.viewsLabel(currentLang),
                        value = totalViews.toString(),
                        icon = Icons.Default.Visibility,
                        tintColor = GimbiGreenPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    MetricBox(
                        title = Translation.callsLabel(currentLang),
                        value = totalCalls.toString(),
                        icon = Icons.Default.Call,
                        tintColor = Color(0xFF1E88E5),
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MetricBox(
                        title = Translation.whatsappClicksLabel(currentLang),
                        value = totalWhatsapp.toString(),
                        icon = Icons.Default.CheckCircle,
                        tintColor = Color(0xFF25D366),
                        modifier = Modifier.weight(1f)
                    )
                    MetricBox(
                        title = Translation.profileVisitsLabel(currentLang),
                        value = totalProfileVisits.toString(),
                        icon = Icons.Default.Person,
                        tintColor = GimbiGold,
                        modifier = Modifier.weight(1f)
                    )
                    MetricBox(
                        title = Translation.directionsAction(currentLang),
                        value = totalDirections.toString(),
                        icon = Icons.Default.Navigation,
                        tintColor = GimbiCoffeeBrown,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Customer Inquiries Inbox
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Customer Inquiries (${inquiries.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        if (inquiries.isEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(modifier = Modifier.padding(20.dp), contentAlignment = Alignment.Center) {
                        Text(
                            text = "No inquiries received yet. When users tap 'Send Inquiry' on your ads, messages will arrive here.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(inquiries) { inquiry ->
                InquiryItemCard(inquiry = inquiry, context = context)
            }
        }

        // Managed Listings
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Advertiser Managed Listings",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        items(myAds) { ad ->
            ManagedAdCard(
                ad = ad,
                currentLanguage = currentLang,
                onClick = { onAdClick(ad.id) },
                onBoost = {
                    Toast.makeText(context, "Listing boosted to Premium package!", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}

@Composable
fun MetricBox(
    title: String,
    value: String,
    icon: ImageVector,
    tintColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = tintColor,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = tintColor
            )
        }
    }
}

@Composable
fun InquiryItemCard(inquiry: Inquiry, context: Context) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = inquiry.senderName,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Phone: ${inquiry.senderPhone}",
                    style = MaterialTheme.typography.labelSmall,
                    color = GimbiGreenPrimary
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Regarding: ${inquiry.adTitle}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "\"${inquiry.message}\"",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = {
                        val intent = Intent(Intent.ACTION_DIAL).apply {
                            data = Uri.parse("tel:${inquiry.senderPhone.replace(" ", "")}")
                        }
                        context.startActivity(intent)
                    },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Reply Call", style = MaterialTheme.typography.labelSmall)
                }
                OutlinedButton(
                    onClick = {
                        val num = inquiry.senderPhone.replace("+", "").replace(" ", "")
                        val uri = Uri.parse("https://api.whatsapp.com/send?phone=$num&text=${Uri.encode("Hello ${inquiry.senderName}, replying to your message on Gimbi Local.")}")
                        context.startActivity(Intent(Intent.ACTION_VIEW, uri))
                    },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Reply WhatsApp", style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}

@Composable
fun ManagedAdCard(
    ad: AdvertisementEntity,
    currentLanguage: com.example.model.AppLanguage,
    onClick: () -> Unit,
    onBoost: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = ad.getTitle(currentLanguage),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (ad.packageTier == "PREMIUM") GimbiGold else GimbiGreenContainer
                ) {
                    Text(
                        text = ad.packageTier,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (ad.packageTier == "PREMIUM") Color.White else GimbiGreenPrimary,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${ad.businessName} • ${ad.locationKebele}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "${ad.viewsCount} views • ${ad.callsCount} calls",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Button(
                    onClick = onBoost,
                    colors = ButtonDefaults.buttonColors(containerColor = GimbiGold),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(imageVector = Icons.Default.RocketLaunch, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Boost Ad", style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}
