package com.shivam.moviejetpack.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shivam.moviejetpack.R


@Composable
fun TopAppBar(modifier: Modifier = Modifier) {

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = "Now Showing",
            modifier = Modifier.weight(1f),
            style = TextStyle(
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            )
        )

        Image(
            painter = painterResource(R.drawable.topbar_right),
            contentDescription = null,
            modifier = Modifier.size(38.dp)
        )

    }

}


@Preview(showBackground = true)
@Composable
fun TopAppBarPreview() {

    TopAppBar()

}