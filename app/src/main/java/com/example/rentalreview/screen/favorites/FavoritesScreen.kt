package com.example.rentalreview.screen.favorites

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.rentalreview.R
import com.example.rentalreview.model.Address
import com.example.rentalreview.model.Comments
import com.example.rentalreview.screen.home.ReviewUiState
import com.example.rentalreview.ui.theme.RentalReviewTheme

@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel = hiltViewModel(),
    onBackClick: () -> Unit
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(Modifier.fillMaxSize()) {
        Text("Favorites",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.padding(start = 10.dp, top = 5.dp),
            color = MaterialTheme.colorScheme.primary
        )
        IconButton(onClick = onBackClick, modifier = Modifier.padding(top = 10.dp )) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back"
            )
        }
        ReviewsList(
            reviews = uiState.reviews,
            userId = uiState.userId
        )
    }
}

@Composable
fun ReviewsList(
    reviews: List<ReviewUiState?>,
    userId: String
){
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .testTag("reviewCard")
    ) {
        if (reviews.isEmpty()) {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "None review find",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                        , fontFamily = FontFamily.Monospace
                    )
                }
            }
        } else {
            itemsIndexed(reviews) { index, review ->
                ReviewCardVisualizer(
                    review = review!!,
                    onLoadComments = {},
                    onShowCommentChange = {},
                    showOtherUsersComments = false,
                    onFavorite = {},
                    userId = userId
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }

}

@Composable
fun ReviewCardVisualizer(
    review: ReviewUiState,
    onLoadComments: () -> Unit,
    onShowCommentChange: () -> Unit,
    showOtherUsersComments: Boolean,
    onFavorite: () -> Unit,
    userId: String
){
    Column(
        Modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.medium
            )
            .wrapContentHeight()
            .background(MaterialTheme.colorScheme.background)
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = R.drawable.chatgpt_image_12_de_mai__de_2025__21_11_19),
            contentDescription = "House",
            modifier = Modifier.size(300.dp),
        )
        Text(review.title,
            fontSize = 23.sp,
            textAlign = TextAlign.Left,
            modifier = Modifier
                .padding(start = 10.dp)
                .fillMaxWidth(),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text("${review.address.street}," +
                " ${review.address.number}," +
                " ${review.address.city}," +
                " ${review.address.state}," +
                " ${review.address.country}",
            fontSize = 17.sp,
            textAlign = TextAlign.Left,
            fontStyle = FontStyle.Italic,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier
                .padding(start = 10.dp, top = 5.dp)
                .fillMaxWidth(),
            color = MaterialTheme.colorScheme.primary
        )
        Row(Modifier
            .padding(start = 10.dp, top = 5.dp)
            .fillMaxWidth()) {
            for (i in 1..5){
                if(i <= review.rating){
                    Icon(imageVector = Icons.Default.Star, contentDescription = "Star",
                        tint = MaterialTheme.colorScheme.primary)
                }else{
                    Icon(imageVector = Icons.Default.Star, contentDescription = "Star")
                }
            }
        }
        Text(
            review.review,
            textAlign = TextAlign.Left,
            modifier = Modifier.padding(10.dp),
            color = MaterialTheme.colorScheme.primary
        )
        Row(
            Modifier
                .padding(start = 10.dp, top = 5.dp, end = 10.dp)
                .fillMaxWidth()
                .height(30.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically)
            {
                Icon(imageVector = Icons.Default.Email, contentDescription = "Comments", modifier = Modifier.clickable { onShowCommentChange() })
                Text("Comment", modifier = Modifier.padding(start = 5.dp), color = MaterialTheme.colorScheme.primary)

            }
            Row(horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {
                    onFavorite()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Favorite",
                    tint = if (review.favoriteIds.contains(userId)) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
                )
                Text("Favorite", modifier = Modifier.padding(start = 5.dp), color = MaterialTheme.colorScheme.primary)
            }
        }
        if (review.showComment) {
            CommentSectionWithOutEntry(
                comments = review.comments,
                onLoadComments = onLoadComments,
                showOtherUserComments = showOtherUsersComments,
            )
        }
    }
}

@Composable
fun CommentSectionWithOutEntry(
    comments: List<Comments>,
    onLoadComments: () -> Unit = {},
    showOtherUserComments: Boolean,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        ) {
            itemsIndexed(comments) { _,comment ->
                if(showOtherUserComments){
                    Text(
                        "Comment ${comment.comment}",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(4.dp)
                    )
                }else{
                    Text(
                        comment.comment,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "Load more comments",
            color = MaterialTheme.colorScheme.secondary,
            fontSize = 16.sp,
            modifier = Modifier
                .padding(5.dp)
                .clickable {
                    onLoadComments()
                }
        )

    }
}

@Preview
@Composable
fun ReviewCardVisualizerPreview(){
    Surface {
        RentalReviewTheme(darkTheme = false, dynamicColor = false) {
            Column(modifier = Modifier.fillMaxSize()) {
                Text("Favorites",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(start = 10.dp, top = 5.dp)
                )
                ReviewsList(
                    reviews = listOf(
                        ReviewUiState(address = Address()),
                        ReviewUiState(address = Address()),
                        ReviewUiState(address = Address()),
                        ReviewUiState(address = Address()),
                        ReviewUiState(address = Address()),
                    ),
                    userId = "0"
                )
            }
        }
    }
}

@Preview
@Composable
fun CommentSectionWithOutEntryPreview(){
    Surface {
        RentalReviewTheme(darkTheme = false, dynamicColor = false) {
            CommentSectionWithOutEntry(
                comments = listOf(
                    Comments("Comment 1"),
                    Comments("Comment 2"),
                    Comments("Comment 3"),
                    Comments("Comment 4"),
                    Comments("Comment 5")
                ),
                onLoadComments = {},
                showOtherUserComments = true
            )
        }
    }
}

@Preview
@Composable
fun FavoritesScreenPreview(){
    ReviewCardVisualizer(
        review = ReviewUiState(address = Address()),
        onLoadComments = {},
        onShowCommentChange = {},
        showOtherUsersComments = false,
        onFavorite = {},
        userId = "0"
    )
}