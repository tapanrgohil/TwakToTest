package com.tapan.twaktotest.ui.userslist.adapter

import android.media.Image
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Card
import androidx.compose.material.Colors
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.skydoves.landscapist.coil.CoilImage
import com.tapan.twaktotest.R

@ExperimentalAnimationApi
@Composable
fun UserAdapterView(
    profileImage: String? = null,
    name: String? = "",
    bio: String? = "",
    isNoteVisible: Boolean = false
) {

    Card(
        Modifier
            .padding(4.dp)
            .zIndex(8f)
            .fillMaxWidth()
            .requiredHeight(60.dp),
        elevation = 8.dp,


    ) {
            ConstraintLayout() {
                val (image, coloumn, icon) = createRefs()

                Image(
                    painter = rememberImagePainter(
                        data = profileImage,
                        builder = {
                            placeholder(drawableResId = R.mipmap.ic_launcher)
                        }),
                    null,
                    contentScale = ContentScale.Inside,
                    modifier = Modifier
                        .constrainAs(image) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            height = Dimension.fillToConstraints
                            width = Dimension.wrapContent
                        }
                        .fillMaxHeight()

                )
                Column(
                    modifier = Modifier
                        .constrainAs(coloumn) {
                            start.linkTo(image.end)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            if (isNoteVisible)
                                end.linkTo(icon.start, margin = Dp(8f))
                            else
                                end.linkTo(parent.end)
                            width = Dimension.fillToConstraints
                        }
                        .padding(
                            vertical = Dp(8f),
                            horizontal = Dp(8f)
                        ),
                    horizontalAlignment = Alignment.Start,
                ) {
                    Text(text = name ?: "", maxLines = 2,modifier = Modifier.width(IntrinsicSize.Max))
                    Text(text = bio ?: "",modifier = Modifier.width(IntrinsicSize.Max))
                }

                androidx.compose.animation.AnimatedVisibility(
                    visible = isNoteVisible,
                    enter = fadeIn(),
                    exit = fadeOut(),
                    modifier = Modifier
                        .padding(Dp(8f))
                        .constrainAs(icon) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            end.linkTo(parent.end)
                        },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_note_24),
                        contentDescription = null,

                        )
                }

            }

    }


}

@ExperimentalAnimationApi
@Preview("MyScreen preview", showSystemUi = true)
@Composable
fun previewUserAdapterView() {
    UserAdapterView(
        name = "tapan tapantapan tapan tapan tapan tapan tapan tapan ",
        bio = "bio",
        isNoteVisible = false
    )
}