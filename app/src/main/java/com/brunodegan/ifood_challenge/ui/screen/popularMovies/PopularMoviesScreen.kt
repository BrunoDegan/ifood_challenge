package com.brunodegan.ifood_challenge.ui.screen.popularMovies

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import coil.size.Scale
import com.brunodegan.ifood_challenge.R
import com.brunodegan.ifood_challenge.base.ui.ErrorUiState
import com.brunodegan.ifood_challenge.base.ui.LoaderUiState
import com.brunodegan.ifood_challenge.base.ui.ObserveAsEvent
import com.brunodegan.ifood_challenge.base.ui.PosterShape
import com.brunodegan.ifood_challenge.base.ui.SnackbarUiStateHolder
import com.brunodegan.ifood_challenge.data.datasources.local.entities.PopularMoviesEntity
import com.brunodegan.ifood_challenge.data.metrics.TrackScreen
import com.brunodegan.ifood_challenge.ui.screen.popularMovies.events.PopularMoviesUiEvents
import com.brunodegan.ifood_challenge.ui.screen.popularMovies.state.PopularMoviesUiState
import com.brunodegan.ifood_challenge.ui.screen.popularMovies.viewModel.PopularMoviesViewModel
import org.koin.compose.viewmodel.koinViewModel

private const val SCREEN_NAME = "PopularMoviesScreen"

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.PopularMoviesScreen(
    modifier: Modifier = Modifier,
    animatedVisibilityScope: AnimatedVisibilityScope,
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
    listState: LazyListState,
    onNavigateUp: () -> Unit,
    onShowSnackbar: (String) -> Unit,
    viewModel: PopularMoviesViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BackHandler {
        onNavigateUp()
    }

    ObserveAsEvent(flow = viewModel.snackbarState) { event ->
        when (event) {
            is SnackbarUiStateHolder.SnackbarUi -> {
                onShowSnackbar(event.msg)
            }
        }
    }

    PopularMoviesScreenContent(
        state = uiState,
        listState = listState,
        scrollBehavior = scrollBehavior,
        modifier = modifier.sharedBounds(
            sharedContentState = rememberSharedContentState(key = uiState),
            animatedVisibilityScope = animatedVisibilityScope,
            enter = slideInVertically(animationSpec = tween(800)),
            exit = slideOutVertically(),
            resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds()
        )
    ) {
        viewModel.onUiEvent(event = it)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PopularMoviesScreenContent(
    listState: LazyListState,
    scrollBehavior: TopAppBarScrollBehavior,
    state: PopularMoviesUiState,
    modifier: Modifier,
    onEvent: (PopularMoviesUiEvents) -> Unit,
) {
    when (state) {
        is PopularMoviesUiState.Initial -> {
            TrackScreen(screenName = SCREEN_NAME)
        }

        is PopularMoviesUiState.Loading -> {
            LoaderUiState()
        }

        is PopularMoviesUiState.Success -> {
            SuccessState(
                listState = listState,
                scrollBehavior = scrollBehavior,
                modifier = modifier,
                viewData = state.viewData,
                onFavoriteButtonClicked = {
                    onEvent(PopularMoviesUiEvents.OnAddFavButtonClickedUiEvent(it))
                },
                onRemoveFavButtonClickedUiEvent = {
                    onEvent(PopularMoviesUiEvents.OnRemoveFavButtonClickedUiEvent(it))
                }
            )
        }

        is PopularMoviesUiState.Error -> {
            ErrorUiState(
                modifier = modifier,
                errorData = state.error,
            ) {
                onEvent(PopularMoviesUiEvents.OnRetryButtonClickedUiEvent)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SuccessState(
    modifier: Modifier,
    listState: LazyListState,
    scrollBehavior: TopAppBarScrollBehavior,
    viewData: List<PopularMoviesEntity>,
    onFavoriteButtonClicked: (Int) -> Unit,
    onRemoveFavButtonClickedUiEvent: (Int) -> Unit
) {

    LazyColumn(
        state = listState,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        flingBehavior = ScrollableDefaults.flingBehavior(),
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .fillMaxSize()
    ) {
        items(viewData.size, key = { index ->
            "${viewData[index].id} -  ${viewData[index].title}"
        }) { position ->
            PopularMoviesCard(
                viewData = viewData[position],
                onFavoriteButtonClicked = {
                    onFavoriteButtonClicked(it)
                },
                onRemoveFavButtonClickedUiEvent = {
                    onRemoveFavButtonClickedUiEvent(it)
                }
            )
        }
    }
}

@Composable
private fun PopularMoviesCard(
    modifier: Modifier = Modifier,
    viewData: PopularMoviesEntity,
    onFavoriteButtonClicked: (Int) -> Unit,
    onRemoveFavButtonClickedUiEvent: (Int) -> Unit
) {
    var isFavoriteButtonClicked by rememberSaveable { mutableStateOf(viewData.isFavorite) }

    val imageRequest = ImageRequest.Builder(LocalContext.current)
        .data(viewData.posterPath)
        .decoderFactory(SvgDecoder.Factory())
        .scale(Scale.FIT)
        .crossfade(true)
        .placeholder(R.drawable.movie_icon)
        .error(R.drawable.error_img)
        .memoryCacheKey(viewData.posterPath)
        .diskCacheKey(viewData.posterPath)
        .build()

    val cardColor by animateColorAsState(
        targetValue = if (isFavoriteButtonClicked == true) {
            MaterialTheme.colorScheme.onSecondary
        } else {
            MaterialTheme.colorScheme.primaryContainer
        }, animationSpec = spring(
            stiffness = Spring.StiffnessLow,
            dampingRatio = Spring.DampingRatioHighBouncy,
        ), label = "animation"
    )

    Card(
        colors = CardDefaults.elevatedCardColors(cardColor),
        shape = RectangleShape,
        elevation = CardDefaults.cardElevation(dimensionResource(R.dimen.card_elevation)),
        border = BorderStroke(
            dimensionResource(R.dimen.card_border_elevation), MaterialTheme.colorScheme.tertiary
        ),
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(all = dimensionResource(R.dimen.card_padding))
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .testTag(stringResource(R.string.popular_movies_card_tag) + " " + viewData.id)
    ) {
        Image(
            painter = painterResource(
                if (isFavoriteButtonClicked == true) {
                    R.drawable.added_to_favorites
                } else {
                    R.drawable.not_added_to_favorites
                }
            ),
            contentDescription = stringResource(R.string.add_to_favorites) + " " + viewData.id,
            modifier = Modifier
                .align(Alignment.End)
                .size(dimensionResource(R.dimen.favorite_icon_size))
                .padding(top = dimensionResource(R.dimen.double_padding))
                .clickable {
                    isFavoriteButtonClicked = isFavoriteButtonClicked.not()
                    if (isFavoriteButtonClicked == true) {
                        onFavoriteButtonClicked(viewData.id)
                    } else {
                        onRemoveFavButtonClickedUiEvent(viewData.id)
                    }
                }
        )
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .padding(start = dimensionResource(R.dimen.double_padding))
                .wrapContentSize()
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(
                        top = dimensionResource(R.dimen.double_padding),
                        bottom = dimensionResource(R.dimen.base_padding)
                    )
            ) {
                AsyncImage(
                    model = imageRequest,
                    fallback = painterResource(R.drawable.movie_icon),
                    error = painterResource(R.drawable.error_img),
                    contentDescription = stringResource(R.string.popular_movies) + " " + viewData.id,
                    filterQuality = FilterQuality.Low,
                    modifier = Modifier
                        .size(
                            dimensionResource(R.dimen.movie_poster_size),
                            dimensionResource(R.dimen.movie_poster_size)
                        )

                        .clip(PosterShape())
                        .fillMaxWidth()
                )
            }

            Text(
                text = viewData.title,
                fontWeight = FontWeight.W600,
                fontSize = TextUnit(
                    value = ResourcesCompat.getFloat(
                        LocalContext.current.resources,
                        R.dimen.movie_title_font_size
                    ), type = TextUnitType.Sp
                ),
                textAlign = TextAlign.Justify,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(
                    top = dimensionResource(R.dimen.double_padding),
                    start = dimensionResource(R.dimen.base_padding)
                )
            )
            Text(
                text = viewData.overview,
                color = MaterialTheme.colorScheme.tertiary,
                fontWeight = FontWeight.W400,
                fontSize = TextUnit(
                    value = ResourcesCompat.getFloat(
                        LocalContext.current.resources,
                        R.dimen.movie_overview_font_size
                    ), type = TextUnitType.Sp
                ),
                textAlign = TextAlign.Justify,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(
                    dimensionResource(R.dimen.base_padding),
                )
            )
            Text(
                text = stringResource(R.string.movie_language).format(
                    viewData.originalLanguage.toUpperCase(Locale.current)
                ),
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium,
                fontSize = TextUnit(
                    value = ResourcesCompat.getFloat(
                        LocalContext.current.resources,
                        R.dimen.movie_language_font_size
                    ), type = TextUnitType.Sp
                ),
                textAlign = TextAlign.Justify,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(
                    dimensionResource(R.dimen.base_padding)
                )
            )
            Text(
                text = stringResource(R.string.movie_release_date).format(
                    viewData.releaseDate
                ),
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Medium,
                fontSize = TextUnit(
                    value = ResourcesCompat.getFloat(
                        LocalContext.current.resources,
                        R.dimen.movie_language_font_size
                    ), type = TextUnitType.Sp
                ),
                textAlign = TextAlign.Justify,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(
                    dimensionResource(R.dimen.base_padding)
                )
            )
            Text(
                text = stringResource(R.string.movie_popularity).format(
                    viewData.popularity.toString()
                ),
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Medium,
                fontSize = TextUnit(
                    value = ResourcesCompat.getFloat(
                        LocalContext.current.resources,
                        R.dimen.movie_language_font_size
                    ), type = TextUnitType.Sp
                ),
                textAlign = TextAlign.Justify,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(
                    dimensionResource(R.dimen.base_padding)
                )
            )

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = stringResource(R.string.movie_vote_average),
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Medium,
                    fontSize = TextUnit(
                        value = ResourcesCompat.getFloat(
                            LocalContext.current.resources,
                            R.dimen.movie_language_font_size
                        ), type = TextUnitType.Sp
                    ),
                    textAlign = TextAlign.Justify,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(
                        dimensionResource(R.dimen.base_padding)
                    )
                )

                LazyRow(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(
                        top = dimensionResource(R.dimen.base_padding),
                        bottom = dimensionResource(R.dimen.double_padding)
                    )
                ) {
                    items(viewData.voteAverage.toInt()) { _ ->
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = stringResource(R.string.movie_vote_average),
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(
                                start = dimensionResource(R.dimen.small_padding)
                            )
                        )
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun PopularMoviesScreenPreview() {
    PopularMoviesScreenContent(
        listState = rememberLazyListState(),
        state = PopularMoviesUiState.Success(
            viewData = listOf(
                PopularMoviesEntity(
                    id = 0,
                    title = "title",
                    posterPath = "posterPath",
                    overview = "overview",
                    originalLanguage = "originalLanguage",
                    popularity = 10.0,
                    voteAverage = 7.5,
                    releaseDate = "24/04/2025",
                    isFavorite = false
                )
            )
        ),
        onEvent = {},
        modifier = Modifier,
        scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    )
}