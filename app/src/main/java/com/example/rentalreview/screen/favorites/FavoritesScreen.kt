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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import coil.compose.AsyncImage
import com.example.rentalreview.R
import com.example.rentalreview.model.Address
import com.example.rentalreview.model.Comments
import com.example.rentalreview.screen.home.ReviewUiState
import com.example.rentalreview.ui.theme.RentalReviewTheme

@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel = hiltViewModel(),
    onReviewClick: (String) -> Unit,
    onBackClick: () -> Unit
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(Modifier.fillMaxSize().statusBarsPadding()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
            Text(
                "Favorites",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = MaterialTheme.colorScheme.primary
            )
        }

        ReviewsList(
            reviews = uiState.reviews,
            onLike = viewModel::likeReview,
            onUnlike = viewModel::unlikeReview,
            userId = uiState.userId,
            onLoadComments = {},
            onSendComment = viewModel::addNewComment,
            comment = uiState.comment,
            onCommentChange = viewModel::onCommentChange,
            onShowCommentChange = viewModel::onShowCommentClick,
            showOtherUsersComments = uiState.showOtherUsersComments,
            onRemoveFavorite = viewModel::removeFavorite,
            onReviewClick = onReviewClick
        )
    }
}

@Composable
fun ReviewsList(
    reviews: List<ReviewUiState?>,
    onLike: (id: String, index: Int) -> Unit,
    onUnlike: (id: String, index: Int) -> Unit,
    userId: String,
    onLoadComments: () -> Unit = {},
    onSendComment: (reviewId: String, index: Int) -> Unit,
    comment: String,
    onCommentChange: (String) -> Unit,
    onShowCommentChange: (index: Int) -> Unit,
    showOtherUsersComments: Boolean,
    onRemoveFavorite: (reviewId: String, index: Int) -> Unit,
    onReviewClick: (String) -> Unit
){
    if(reviews.isEmpty()){
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "No favorite reviews yet",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 20.sp,
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .testTag("reviewCard"),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(reviews) { index, review ->
                review?.let {
                    ReviewCard(
                        review = it,
                        onLike = { onLike(it.id, index) },
                        onUnlike = { onUnlike(it.id, index) },
                        userId = userId,
                        onLoadComments = onLoadComments,
                        onSendComment = { onSendComment(it.id, index) },
                        comment = comment,
                        onCommentChange = onCommentChange,
                        onShowCommentChange = { onShowCommentChange(index) },
                        showOtherUsersComments = showOtherUsersComments,
                        onRemoveFavorite = { onRemoveFavorite(it.id, index) },
                        onReviewClick = { onReviewClick(it.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun ReviewCard(
    review: ReviewUiState,
    onLike: () -> Unit,
    onUnlike: () -> Unit,
    userId: String,
    onLoadComments: () -> Unit,
    onSendComment: () -> Unit,
    comment: String,
    onCommentChange: (String) -> Unit,
    onShowCommentChange: () -> Unit,
    showOtherUsersComments: Boolean,
    onRemoveFavorite: () -> Unit,
    onReviewClick: () -> Unit
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
            .padding(10.dp)
            .testTag("reviewCard")
            .clickable { onReviewClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Display images
        if(review.imageUrl.isNotEmpty()){
            LazyRow {
                items(review.imageUrl) {
                    AsyncImage(
                        model = it,
                        contentDescription = "House Image",
                        modifier = Modifier.size(300.dp)
                    )
                }
            }
        } else {
            Image(
                painter = painterResource(id = R.drawable.chatgpt_image_12_de_mai__de_2025__21_11_19),
                contentDescription = "House",
                modifier = Modifier.size(300.dp),
            )
        }

        Text(
            review.title,
            fontSize = 23.sp,
            textAlign = TextAlign.Left,
            modifier = Modifier
                .padding(start = 10.dp)
                .fillMaxWidth(),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            "${review.address.street}, ${review.address.number}, ${review.address.city}, ${review.address.state}, ${review.address.country}",
            fontSize = 17.sp,
            textAlign = TextAlign.Left,
            fontStyle = FontStyle.Italic,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier
                .padding(start = 10.dp, top = 5.dp)
                .fillMaxWidth(),
            color = MaterialTheme.colorScheme.primary
        )

        // Rating stars
        Row(
            Modifier
                .padding(start = 10.dp, top = 5.dp)
                .fillMaxWidth()
        ) {
            for (i in 1..5){
                if(i <= review.rating){
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Star",
                        tint = MaterialTheme.colorScheme.primary
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Star"
                    )
                }
            }
        }

        Text(
            review.review,
            textAlign = TextAlign.Left,
            modifier = Modifier.padding(10.dp),
            color = MaterialTheme.colorScheme.primary
        )

        // Action buttons: Like, Comment, Remove from Favorites
        Row(
            Modifier
                .padding(start = 10.dp, top = 5.dp, end = 10.dp)
                .fillMaxWidth()
                .height(30.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Like button
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {
                    if (review.likesIds.contains(userId)) onUnlike()
                    else onLike()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.ThumbUp,
                    contentDescription = "Like",
                    tint = if (review.likesIds.contains(userId))
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onBackground
                )
                Text("Like", modifier = Modifier.padding(start = 5.dp), color = MaterialTheme.colorScheme.primary)
            }

            // Comment button
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onShowCommentChange() }
            ) {
                Icon(imageVector = Icons.Default.Email, contentDescription = "Comments")
                Text("Comment", modifier = Modifier.padding(start = 5.dp), color = MaterialTheme.colorScheme.primary)
            }

            // Remove from favorites button
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onRemoveFavorite() }
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Remove from Favorites",
                    tint = MaterialTheme.colorScheme.primary
                )
                Text("Remove", modifier = Modifier.padding(start = 5.dp), color = MaterialTheme.colorScheme.primary)
            }
        }

        // Display comments section if expanded
        if (review.showComment) {
            CommentSection(
                comments = review.comments,
                onLoadComments = onLoadComments,
                onSendComment = onSendComment,
                comment = comment,
                onCommentChange = onCommentChange,
                showOtherUserComments = showOtherUsersComments,
            )
        }
    }
}

@Composable
fun CommentSection(
    comments: List<Comments>,
    onLoadComments: () -> Unit = {},
    onSendComment: () -> Unit,
    comment: String,
    onCommentChange: (String) -> Unit,
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
            itemsIndexed(comments) { _, commentItem ->
                if(showOtherUserComments){
                    Text(
                        "Comment ${commentItem.comment}",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(4.dp)
                    )
                } else {
                    Text(
                        commentItem.comment,
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
                .clickable { onLoadComments() }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = comment,
                onValueChange = onCommentChange,
                modifier = Modifier
                    .padding(5.dp)
                    .weight(1f)
            )
            OutlinedButton(
                onClick = onSendComment,
                modifier = Modifier.padding(5.dp)
            ) {
                Text("Send")
            }
        }
    }
}

@Preview
@Composable
fun ReviewCardPreview(){
    Surface {
        RentalReviewTheme(darkTheme = false, dynamicColor = false) {
            ReviewCard(
                review = ReviewUiState(
                    comments = mutableListOf(
                        Comments("1", "Test Comment"),
                        Comments("1", "Test Comment")
                    ),
                    address = Address(),
                    imageUrl = emptyList()
                ),
                onLike = {},
                userId = "1",
                onUnlike = {},
                onLoadComments = {},
                onSendComment = {},
                comment = "",
                onCommentChange = {},
                onShowCommentChange = {},
                showOtherUsersComments = false,
                onRemoveFavorite = {},
                onReviewClick = {}
            )
        }
    }
}

@Preview
@Composable
fun FavoritesScreenPreview(){
    Surface {
        RentalReviewTheme(darkTheme = false, dynamicColor = false) {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    "Favorites",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(start = 10.dp, top = 5.dp)
                )
                ReviewsList(
                    reviews = emptyList(),
                    onLike = { _, _ -> },
                    onUnlike = { _, _ -> },
                    userId = "0",
                    onLoadComments = {},
                    onSendComment = { _, _ -> },
                    comment = "",
                    onCommentChange = {},
                    onShowCommentChange = {},
                    showOtherUsersComments = false,
                    onRemoveFavorite = { _, _ -> },
                    onReviewClick = {}
                )
            }
        }
    }
}

