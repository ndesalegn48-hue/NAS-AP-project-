package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.platform.LocalContext
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
import com.example.ui.theme.GimbiCoffeeBrown
import com.example.ui.theme.GimbiGold
import com.example.ui.theme.GimbiGreenContainer
import com.example.ui.theme.GimbiGreenDark
import com.example.ui.theme.GimbiGreenPrimary
import com.example.ui.theme.VerifiedBlue
import com.example.ui.viewmodel.GimbiViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdDetailScreen(
    adId: Long,
    viewModel: GimbiViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    val currentLang by viewModel.currentLanguage.collectAsStateWithLifecycle()
    val allAds by viewModel.filteredAds.collectAsStateWithLifecycle()
    val allAdminAds by viewModel.allAdsAdmin.collectAsStateWithLifecycle()
    val ad = allAds.find { it.id == adId } ?: allAdminAds.find { it.id == adId }

    val context = LocalContext.current
    var showInquiryDialog by remember { mutableStateOf(false) }
    var showReportDialog by remember { mutableStateOf(false) }
    var inquirySenderName by remember { mutableStateOf("") }
    var inquirySenderPhone by remember { mutableStateOf("+251 ") }
    var inquiryMessage by remember { mutableStateOf("") }

    if (ad == null) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier.fillMaxSize()
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Advertisement not found")
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = onBack) { Text("Go Back") }
            }
        }
        return
    }

    val category = Category.values().find { it.id == ad.categoryId } ?: Category.BUSINESS

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 60.dp)
            .testTag("ad_detail_screen")
    ) {
        // Top App Bar with back button
        TopAppBar(
            title = {
                Text(
                    text = ad.businessName,
                    maxLines = 1,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            navigationIcon = {
                IconButton(onClick = onBack, modifier = Modifier.testTag("detail_back_button")) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },
            actions = {
                IconButton(onClick = { viewModel.toggleSave(ad.id, ad.isSaved) }) {
                    Icon(
                        imageVector = if (ad.isSaved) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Save",
                        tint = if (ad.isSaved) Color(0xFFFF5252) else MaterialTheme.colorScheme.onSurface
                    )
                }
                IconButton(onClick = {
                    val shareText = "${ad.getTitle(currentLang)} by ${ad.businessName} on Gimbi Local. Contact: ${ad.phoneNumber}"
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, shareText)
                    }
                    context.startActivity(Intent.createChooser(intent, "Share listing"))
                }) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = "Share")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )

        // Hero Image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
        ) {
            when (ad.imageResName) {
                "img_coffee_agri" -> {
                    Image(
                        painter = painterResource(id = R.drawable.img_coffee_agri),
                        contentDescription = ad.getTitle(currentLang),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                "img_gimbi_hero" -> {
                    Image(
                        painter = painterResource(id = R.drawable.img_gimbi_hero),
                        contentDescription = ad.getTitle(currentLang),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                else -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.linearGradient(
                                    listOf(GimbiGreenDark, GimbiCoffeeBrown)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Surface(
                                shape = CircleShape,
                                color = Color.White.copy(alpha = 0.2f),
                                modifier = Modifier.size(64.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = category.getLocalizedName(currentLang).take(2).uppercase(),
                                        color = Color.White,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 24.sp
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = category.getLocalizedName(currentLang),
                                color = Color.White,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            }

            // Category & Package Overlay
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .align(Alignment.BottomStart),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.Black.copy(alpha = 0.7f)
                ) {
                    Text(
                        text = category.getLocalizedName(currentLang),
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }

                if (ad.isVerified) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = VerifiedBlue
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = "Verified",
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = Translation.verifiedBadge(currentLang),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }

        // Details Container
        Column(modifier = Modifier.padding(16.dp)) {
            // Price & Title
            if (ad.priceText.isNotBlank()) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = ad.priceText,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            Text(
                text = ad.getTitle(currentLang),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Business & Verified Info
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = ad.businessName,
                    style = MaterialTheme.typography.titleMedium,
                    color = GimbiGreenPrimary,
                    fontWeight = FontWeight.SemiBold
                )
                if (ad.isVerified) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Verified",
                        tint = VerifiedBlue,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Location Kebele
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${ad.locationKebele}, Gimbi, West Welega",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Contact Action Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        viewModel.trackCall(ad.id)
                        dialPhone(context, ad.phoneNumber)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GimbiGreenPrimary),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f).testTag("detail_call_button")
                ) {
                    Icon(imageVector = Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(Translation.callAction(currentLang), fontWeight = FontWeight.Bold)
                }

                if (ad.whatsappNumber.isNotBlank()) {
                    Button(
                        onClick = {
                            viewModel.trackWhatsapp(ad.id)
                            openWhatsApp(context, ad.whatsappNumber, ad.getTitle(currentLang))
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f).testTag("detail_whatsapp_button")
                    ) {
                        Text("WhatsApp", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Send Direct Inquiry Button & Directions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { showInquiryDialog = true },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f).testTag("detail_inquiry_button")
                ) {
                    Icon(imageVector = Icons.Default.Message, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(Translation.sendInquiryAction(currentLang))
                }

                OutlinedButton(
                    onClick = {
                        viewModel.trackDirections(ad.id)
                        val geoUri = Uri.parse("geo:0,0?q=${Uri.encode("${ad.locationKebele}, Gimbi, Ethiopia")}")
                        val mapIntent = Intent(Intent.ACTION_VIEW, geoUri)
                        try {
                            context.startActivity(mapIntent)
                        } catch (_: Exception) {
                            Toast.makeText(context, "Location: ${ad.locationKebele}, Gimbi", Toast.LENGTH_LONG).show()
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(imageVector = Icons.Default.Navigation, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(Translation.directionsAction(currentLang))
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            // About Description
            Text(
                text = "About this Advertisement",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = ad.getDescription(currentLang),
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Advertiser Contact Details Box
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Advertiser Contact Info",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp), tint = GimbiGreenPrimary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = ad.phoneNumber, style = MaterialTheme.typography.bodyMedium)
                    }
                    if (ad.email.isNotBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Email, contentDescription = null, modifier = Modifier.size(16.dp), tint = GimbiGreenPrimary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = ad.email, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Safety & Trust Box
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF3E5F5)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Shield, contentDescription = null, tint = Color(0xFF6A1B9A), modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = Translation.safetyTitle(currentLang),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4A148C)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = Translation.safetyDescription(currentLang),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF4A148C).copy(alpha = 0.85f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextButton(onClick = { showReportDialog = true }) {
                        Icon(imageVector = Icons.Default.Report, contentDescription = null, tint = Color(0xFFB71C1C), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Report this listing to moderation", color = Color(0xFFB71C1C), style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }
    }

    // Inquiry Dialog
    if (showInquiryDialog) {
        AlertDialog(
            onDismissRequest = { showInquiryDialog = false },
            title = { Text("Send Inquiry to ${ad.businessName}") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Your message will be delivered directly to the business owner's Gimbi Local dashboard.")
                    OutlinedTextField(
                        value = inquirySenderName,
                        onValueChange = { inquirySenderName = it },
                        label = { Text("Your Name") },
                        modifier = Modifier.fillMaxWidth().testTag("inquiry_name")
                    )
                    OutlinedTextField(
                        value = inquirySenderPhone,
                        onValueChange = { inquirySenderPhone = it },
                        label = { Text("Your Phone") },
                        modifier = Modifier.fillMaxWidth().testTag("inquiry_phone")
                    )
                    OutlinedTextField(
                        value = inquiryMessage,
                        onValueChange = { inquiryMessage = it },
                        label = { Text("Message / Question") },
                        modifier = Modifier.fillMaxWidth().height(90.dp).testTag("inquiry_msg")
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (inquirySenderName.isNotBlank() && inquiryMessage.isNotBlank()) {
                            viewModel.submitInquiry(
                                adId = ad.id,
                                businessName = ad.businessName,
                                adTitle = ad.getTitle(currentLang),
                                senderName = inquirySenderName,
                                senderPhone = inquirySenderPhone,
                                message = inquiryMessage,
                                onSuccess = {
                                    showInquiryDialog = false
                                    Toast.makeText(context, "Inquiry sent successfully!", Toast.LENGTH_SHORT).show()
                                }
                            )
                        } else {
                            Toast.makeText(context, "Please enter your name and message", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GimbiGreenPrimary),
                    modifier = Modifier.testTag("inquiry_submit_btn")
                ) {
                    Text("Send")
                }
            },
            dismissButton = {
                TextButton(onClick = { showInquiryDialog = false }) { Text("Cancel") }
            }
        )
    }

    // Report Dialog
    if (showReportDialog) {
        AlertDialog(
            onDismissRequest = { showReportDialog = false },
            title = { Text("Report Advertisement") },
            text = {
                Text("Thank you for helping keep Gimbi Local safe. Your report has been submitted to the local community moderation team for review.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showReportDialog = false
                        Toast.makeText(context, "Report logged for moderation review", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GimbiGreenPrimary)
                ) {
                    Text("OK")
                }
            }
        )
    }
}

private fun dialPhone(context: Context, phone: String) {
    try {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:${phone.replace(" ", "")}")
        }
        context.startActivity(intent)
    } catch (_: Exception) {}
}

private fun openWhatsApp(context: Context, number: String, itemTitle: String) {
    try {
        val cleanNumber = number.replace("+", "").replace(" ", "").trim()
        val text = Uri.encode("Hello, I am contacting you regarding your Gimbi Local listing: $itemTitle")
        val url = "https://api.whatsapp.com/send?phone=$cleanNumber&text=$text"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    } catch (_: Exception) {}
}
