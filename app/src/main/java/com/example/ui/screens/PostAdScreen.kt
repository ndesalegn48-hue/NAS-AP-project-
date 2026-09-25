package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.AdType
import com.example.model.AppLanguage
import com.example.model.Category
import com.example.model.PackageTier
import com.example.model.Translation
import com.example.ui.components.PackageSelector
import com.example.ui.theme.GimbiGreenContainer
import com.example.ui.theme.GimbiGreenPrimary
import com.example.ui.viewmodel.GimbiViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostAdScreen(
    viewModel: GimbiViewModel,
    onAdCreated: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val currentLang by viewModel.currentLanguage.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var currentStep by remember { mutableIntStateOf(1) }

    // Form states
    var businessName by remember { mutableStateOf("") }
    var adTitle by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(Category.BUSINESS) }
    var categoryExpanded by remember { mutableStateOf(false) }

    var selectedAdType by remember { mutableStateOf(AdType.PRODUCT) }
    var adTypeExpanded by remember { mutableStateOf(false) }

    var priceText by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("+251 ") }
    var whatsappNumber by remember { mutableStateOf("+251 ") }
    var email by remember { mutableStateOf("") }
    var locationKebele by remember { mutableStateOf("Kebele 01") }
    var kebeleExpanded by remember { mutableStateOf(false) }

    var selectedPackage by remember { mutableStateOf(PackageTier.STANDARD) }
    var selectedPaymentGateway by remember { mutableStateOf("Telebirr") }
    var isSubmitted by remember { mutableStateOf(false) }
    var createdAdId by remember { mutableStateOf(0L) }

    val gimbiKebeles = listOf(
        "Kebele 01 (Central & Stadium)",
        "Kebele 02 (Trade & Warehouse)",
        "Kebele 03 (Residential & Youth Center)",
        "Kebele 04 (Suburbs)",
        "Merkato Commercial District",
        "Gimbi Bus Station Hub (Buufata Konkolaataa)",
        "Hospital & Health Center Road"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .padding(bottom = 80.dp)
            .testTag("post_ad_screen")
    ) {
        if (isSubmitted) {
            // Success Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = GimbiGreenContainer),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Success",
                        tint = GimbiGreenPrimary,
                        modifier = Modifier.size(56.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Advertisement Published!",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = GimbiGreenPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Your ad for '$adTitle' under ${selectedPackage.getLocalizedName(currentLang)} is now live in Gimbi Local.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF00381F)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = { onAdCreated(createdAdId) },
                        colors = ButtonDefaults.buttonColors(containerColor = GimbiGreenPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().testTag("view_created_ad_button")
                    ) {
                        Text("View Advertisement / Ilaali")
                    }
                }
            }
            return
        }

        // Stepper Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Surface(
                shape = CircleShape,
                color = GimbiGreenContainer,
                modifier = Modifier.size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "$currentStep/3",
                        fontWeight = FontWeight.Bold,
                        color = GimbiGreenPrimary,
                        fontSize = 14.sp
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = when (currentStep) {
                        1 -> "Step 1: Listing Details"
                        2 -> "Step 2: Contact & Location"
                        else -> "Step 3: Advertising Package"
                    },
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = Translation.postAdHeadline(currentLang),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (currentStep) {
            1 -> {
                // Step 1: Info & Category
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Business / Institution Name *",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = businessName,
                            onValueChange = { businessName = it },
                            placeholder = { Text("e.g. Gimbi High School, Oromia Coffee Hub") },
                            modifier = Modifier.fillMaxWidth().testTag("input_business_name"),
                            shape = RoundedCornerShape(10.dp)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Advertisement Title *",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = adTitle,
                            onValueChange = { adTitle = it },
                            placeholder = { Text("e.g. Specialty Grade 1 Welega Coffee") },
                            modifier = Modifier.fillMaxWidth().testTag("input_ad_title"),
                            shape = RoundedCornerShape(10.dp)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Category Dropdown
                        Text(
                            text = "Category *",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        ExposedDropdownMenuBox(
                            expanded = categoryExpanded,
                            onExpandedChange = { categoryExpanded = it }
                        ) {
                            OutlinedTextField(
                                value = selectedCategory.getLocalizedName(currentLang),
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                                    .testTag("dropdown_category"),
                                shape = RoundedCornerShape(10.dp)
                            )
                            ExposedDropdownMenu(
                                expanded = categoryExpanded,
                                onDismissRequest = { categoryExpanded = false }
                            ) {
                                Category.values().forEach { cat ->
                                    DropdownMenuItem(
                                        text = { Text(cat.getLocalizedName(currentLang)) },
                                        onClick = {
                                            selectedCategory = cat
                                            categoryExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Ad Type Dropdown
                        Text(
                            text = "Advertisement Type *",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        ExposedDropdownMenuBox(
                            expanded = adTypeExpanded,
                            onExpandedChange = { adTypeExpanded = it }
                        ) {
                            OutlinedTextField(
                                value = selectedAdType.getLocalized(currentLang),
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = adTypeExpanded) },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                                    .testTag("dropdown_ad_type"),
                                shape = RoundedCornerShape(10.dp)
                            )
                            ExposedDropdownMenu(
                                expanded = adTypeExpanded,
                                onDismissRequest = { adTypeExpanded = false }
                            ) {
                                AdType.values().forEach { type ->
                                    DropdownMenuItem(
                                        text = { Text(type.getLocalized(currentLang)) },
                                        onClick = {
                                            selectedAdType = type
                                            adTypeExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Detailed Description *",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            placeholder = { Text("Describe products, services, offers, qualifications...") },
                            modifier = Modifier.fillMaxWidth().height(110.dp).testTag("input_description"),
                            shape = RoundedCornerShape(10.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (businessName.isBlank() || adTitle.isBlank() || description.isBlank()) {
                            Toast.makeText(context, "Please fill required fields", Toast.LENGTH_SHORT).show()
                        } else {
                            currentStep = 2
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GimbiGreenPrimary),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().testTag("step_1_next_button")
                ) {
                    Text("Continue to Contact & Location →")
                }
            }

            2 -> {
                // Step 2: Contact & Location
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Phone Number (Calls & SMS) *",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = phoneNumber,
                            onValueChange = { phoneNumber = it },
                            placeholder = { Text("+251 91 ...") },
                            modifier = Modifier.fillMaxWidth().testTag("input_phone"),
                            shape = RoundedCornerShape(10.dp)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "WhatsApp Number",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = whatsappNumber,
                            onValueChange = { whatsappNumber = it },
                            placeholder = { Text("+251 91 ...") },
                            modifier = Modifier.fillMaxWidth().testTag("input_whatsapp"),
                            shape = RoundedCornerShape(10.dp)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Email Address (Optional)",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            placeholder = { Text("contact@mybusiness.com") },
                            modifier = Modifier.fillMaxWidth().testTag("input_email"),
                            shape = RoundedCornerShape(10.dp)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Location in Gimbi
                        Text(
                            text = "Location in Gimbi (Kebele / Landmark) *",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        ExposedDropdownMenuBox(
                            expanded = kebeleExpanded,
                            onExpandedChange = { kebeleExpanded = it }
                        ) {
                            OutlinedTextField(
                                value = locationKebele,
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = kebeleExpanded) },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                                    .testTag("dropdown_kebele"),
                                shape = RoundedCornerShape(10.dp)
                            )
                            ExposedDropdownMenu(
                                expanded = kebeleExpanded,
                                onDismissRequest = { kebeleExpanded = false }
                            ) {
                                gimbiKebeles.forEach { keb ->
                                    DropdownMenuItem(
                                        text = { Text(keb) },
                                        onClick = {
                                            locationKebele = keb
                                            kebeleExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Price / Fee in ETB (Optional)",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = priceText,
                            onValueChange = { priceText = it },
                            placeholder = { Text("e.g. 500 ETB / kg or Negotiable") },
                            modifier = Modifier.fillMaxWidth().testTag("input_price"),
                            shape = RoundedCornerShape(10.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = { currentStep = 1 },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("← Back")
                    }

                    Button(
                        onClick = {
                            if (phoneNumber.length < 9) {
                                Toast.makeText(context, "Enter a valid phone number", Toast.LENGTH_SHORT).show()
                            } else {
                                currentStep = 3
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GimbiGreenPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(2f).testTag("step_2_next_button")
                    ) {
                        Text("Choose Package →")
                    }
                }
            }

            3 -> {
                // Step 3: Package Selection
                Text(
                    text = Translation.packagesTitle(currentLang),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Select an advertising package to promote your listing to the community of Gimbi.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                PackageSelector(
                    selectedPackage = selectedPackage,
                    currentLanguage = currentLang,
                    onSelectPackage = { selectedPackage = it }
                )

                if (selectedPackage != PackageTier.FREE) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "Ethiopian Payment Method (Chapa / Telebirr)",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                listOf("Telebirr", "CBE Birr", "Chapa Card").forEach { method ->
                                    val isSelected = selectedPaymentGateway == method
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = if (isSelected) GimbiGreenPrimary else MaterialTheme.colorScheme.surface,
                                        modifier = Modifier
                                            .weight(1f)
                                            .clickable { selectedPaymentGateway = method }
                                    ) {
                                        Box(
                                            contentAlignment = Alignment.Center,
                                            modifier = Modifier.padding(vertical = 8.dp)
                                        ) {
                                            Text(
                                                text = method,
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Amount to pay: ${selectedPackage.priceEtb} ETB (Instant activation)",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = { currentStep = 2 },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("← Back")
                    }

                    Button(
                        onClick = {
                            viewModel.createAdvertisement(
                                title = adTitle,
                                description = description,
                                businessName = businessName,
                                category = selectedCategory,
                                adType = selectedAdType,
                                packageTier = selectedPackage,
                                priceText = priceText,
                                phone = phoneNumber,
                                whatsapp = whatsappNumber,
                                email = email,
                                locationKebele = locationKebele,
                                onComplete = { id ->
                                    createdAdId = id
                                    isSubmitted = true
                                }
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GimbiGreenPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(2f).testTag("publish_ad_button")
                    ) {
                        Text("Publish Ad / Beeksisi")
                    }
                }
            }
        }
    }
}
