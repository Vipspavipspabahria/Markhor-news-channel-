package com.example.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.WhatsAppAccentGreen
import com.example.ui.theme.WhatsAppTealDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewDispatchDialog(
    onDismiss: () -> Unit,
    onPublish: (headline: String, summary: String, category: String, content: String) -> Unit
) {
    var headline by remember { mutableStateOf("") }
    var summary by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Defense") }
    var expandedCategoryDropdown by remember { mutableStateOf(false) }

    val categories = listOf("Defense", "Cyber", "Geopolitics", "Intelligence", "National")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Publish Intelligence Dispatch",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = WhatsAppTealDark
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Broadcast breaking news to Markhor News Channel stream",
                    fontSize = 12.sp,
                    color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Category Selector Dropdown
                ExposedDropdownMenuBox(
                    expanded = expandedCategoryDropdown,
                    onExpandedChange = { expandedCategoryDropdown = !expandedCategoryDropdown }
                ) {
                    OutlinedTextField(
                        value = selectedCategory,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Category") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategoryDropdown) },
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                            .testTag("dispatch_category_dropdown")
                    )

                    ExposedDropdownMenu(
                        expanded = expandedCategoryDropdown,
                        onDismissRequest = { expandedCategoryDropdown = false }
                    ) {
                        categories.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat) },
                                onClick = {
                                    selectedCategory = cat
                                    expandedCategoryDropdown = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = headline,
                    onValueChange = { headline = it },
                    label = { Text("Headline / Title *") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("dispatch_headline_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = summary,
                    onValueChange = { summary = it },
                    label = { Text("Brief Summary *") },
                    maxLines = 3,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("dispatch_summary_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Full Intelligence Details (Optional)") },
                    maxLines = 5,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("dispatch_content_input")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onPublish(headline, summary, selectedCategory, content) },
                enabled = headline.isNotBlank() && summary.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = WhatsAppAccentGreen),
                modifier = Modifier.testTag("publish_dispatch_button")
            ) {
                Text("Publish")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        shape = RoundedCornerShape(16.dp)
    )
}
