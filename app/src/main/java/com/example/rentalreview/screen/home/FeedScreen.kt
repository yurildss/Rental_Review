package com.example.rentalreview.screen.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.rentalreview.R
import com.example.rentalreview.model.Comments
import com.example.rentalreview.model.Review
import com.example.rentalreview.screen.perfil.PerfilScreen
import com.example.rentalreview.screen.review.ReviewEntryScreen
import com.example.rentalreview.ui.theme.RentalReviewTheme

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FeedScreen(
    onSave: () -> Unit = {},
    viewModel: FeedScreenViewModel = hiltViewModel()
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val pagerState = rememberPagerState{
        uiState.navItems.size
    }

    LaunchedEffect(uiState.selectedItem) {
        val index = uiState.navItems.indexOf(uiState.selectedItem)
        if (index != -1 && pagerState.currentPage != index) {
            pagerState.animateScrollToPage(index)
        }
    }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {

    }, bottomBar = {
        BottomBar(
            navItems = uiState.navItems,
            selectedItem = uiState.selectedItem,
            onItemClick = viewModel::onNavItemClicked
        )
    }) {
        innerPadding ->
        HorizontalPager(
            pagerState,
            modifier = Modifier.padding(innerPadding)
        ) { page->
            val item = uiState.navItems[page]

            when(item){
                uiState.navItems[0] -> ReviewsList(
                    reviews = uiState.reviews,
                    onLike = viewModel::likeReview,
                    onDesLike = viewModel::unlikeReview,
                    userId = uiState.userId,
                    onLoadNextPage = viewModel::getMoreReviews,
                    onLoadComments = {  },
                    onSendComment = viewModel::comment,
                    comment = uiState.comment,
                    showComments = uiState.showComment,
                    onCommentChange = viewModel::onCommentChange,
                    onShowCommentChange = viewModel::onShowCommentChange
                )
                uiState.navItems[2] -> ReviewEntryScreen(onSaved = onSave)
                uiState.navItems[3] -> PerfilScreen()
            }
        }
    }
}

@Composable
fun ReviewsList(
    reviews: List<Review?>,
    onLike: (id: String, index: Int) -> Unit,
    onDesLike: (id: String, index: Int) -> Unit,
    userId: String,
    onLoadNextPage: () -> Unit = {},
    onLoadComments: () -> Unit,
    onSendComment: (reviewId: String, index: Int) -> Unit,
    comment: String,
    showComments: Boolean,
    onCommentChange: (String) -> Unit,
    onShowCommentChange: (Boolean) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .padding(10.dp)
            .testTag("homeScreen"),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(reviews) { index, review ->

            review?.let {
                ReviewCard(
                    it, { onLike(it.id, index) }, { onDesLike(it.id, index) }, userId,
                    onLoadComments = onLoadComments,
                    onSendComment = { onSendComment(it.id, index) },
                    comment = comment,
                    showComments = showComments,
                    onCommentChange = onCommentChange,
                    onShowCommentChange = onShowCommentChange
                )
            }
            if (index == reviews.lastIndex - 1) {
                onLoadNextPage()
            }
        }

    }
}

@Composable
fun BottomBar(
    navItems: List<NavItem> = listOf(),
    selectedItem: NavItem? = null,
    onItemClick: (NavItem) -> Unit = {}
){
    BottomAppBar(
        modifier = Modifier.background(MaterialTheme.colorScheme.primary),
        actions = {
            navItems.forEach {
                item ->
                NavigationBarItem(
                    selected = selectedItem == item,
                    onClick = {
                        onItemClick(item)
                    },
                    icon = { Icon(imageVector = item.icon, contentDescription = item.description) },
                    modifier = Modifier.testTag(item.testTag)
                )
            }
    })
}

@Composable
fun CommentSection(
    comments: List<Comments> = listOf(),
    onLoadComments: () -> Unit = {},
    onSendComment: () -> Unit,
    comment: String,
    onCommentChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            itemsIndexed(comments) { _,comment ->
                Text(
                    "Comment $comment",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "Load more comments",
            color = MaterialTheme.colorScheme.secondary,
            fontSize = 16.sp,
            modifier = Modifier.padding(5.dp)
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
                onClick =  onSendComment,
                modifier = Modifier.padding(5.dp)
            ) {
                Text("Send")
            }
        }
    }
}


@Composable
fun ReviewCard(
    review: Review,
    onLike: () -> Unit,
    desLike: () -> Unit,
    userId: String,
    onLoadComments: () -> Unit,
    onSendComment: () -> Unit,
    comment: String,
    showComments: Boolean = false,
    onShowCommentChange: (Boolean) -> Unit,
    onCommentChange: (String) -> Unit
){
    Column(
        Modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.medium
            )
            .height(700.dp)
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
        Text(review.address,
            fontSize = 19.sp,
            textAlign = TextAlign.Left,
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
                .padding(start = 10.dp, top = 5.dp)
                .fillMaxWidth()
                .height(30.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.ThumbUp,
                    contentDescription = "Like",
                    tint = if (review.likesIds.contains(userId)) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.clickable{
                        if (review.likesIds.contains(userId)) desLike()
                        else onLike()
                    }

                )
                Text("Like", modifier = Modifier.padding(start = 5.dp), color = MaterialTheme.colorScheme.primary)
            }
            Row(horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically)
            {
                Icon(imageVector = Icons.Default.Email, contentDescription = "Like", modifier = Modifier.clickable { onShowCommentChange(true) })
                Text("Comment", modifier = Modifier.padding(start = 5.dp), color = MaterialTheme.colorScheme.primary)

            }
            Row(horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.ThumbUp, contentDescription = "Like")
                Text("Like", modifier = Modifier.padding(start = 5.dp), color = MaterialTheme.colorScheme.primary)
            }
        }
        if (showComments) {
            CommentSection(
                comments = review.comments,
                onLoadComments = onLoadComments,
                onSendComment = onSendComment,
                comment = comment,
                onCommentChange = onCommentChange
            )
        }
    }
}


@Composable
@Preview
fun ReviewCardPreview(){
    Surface {
        RentalReviewTheme(darkTheme = false, dynamicColor = false) {
            ReviewCard(
                review = Review(
                    comments = mutableListOf(Comments(
                        "1",
                        "Test Comment"
                    )),
                ),
                onLike = {},
                userId = "1",
                desLike = {},
                onLoadComments = {},
                onSendComment = {},
                comment = "",
                showComments = true,
                onCommentChange = {},
                onShowCommentChange = {  }
            )
        }
    }
}

@Composable
@Preview
fun ReviewBlackCardPreview(){
    Surface {
        RentalReviewTheme(darkTheme = true, dynamicColor = false) {
            ReviewCard(
                review = Review(),
                onLike = {},
                userId = "1",
                desLike = {},
                onLoadComments = {},
                onSendComment = {},
                comment = "",
                showComments = true,
                onCommentChange = {},
                onShowCommentChange = {  }
            )
        }
    }
}

@Composable
@Preview
fun BottomBarPreview(){
    Surface {
        RentalReviewTheme(darkTheme = false, dynamicColor = false) {
            BottomBar()
        }
    }
}

@Composable
@Preview
fun BottomBlackBarPreview(){
    Surface {
        RentalReviewTheme(darkTheme = true, dynamicColor = false) {
            BottomBar()
        }
    }
}

@Composable
@Preview
fun CommentSectionPreview(){
    Surface {
        RentalReviewTheme(darkTheme = false, dynamicColor = false) {
            CommentSection(
                comments = listOf(Comments()),
                onLoadComments = {},
                onSendComment = {},
                comment = "",
                onCommentChange = {}
            )
        }
    }
}