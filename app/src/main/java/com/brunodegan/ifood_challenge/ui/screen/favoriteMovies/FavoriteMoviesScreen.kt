package com.brunodegan.ifood_challenge.ui.screen.favoriteMovies

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
import com.brunodegan.ifood_challenge.data.datasources.local.entities.FavoriteMoviesEntity
import com.brunodegan.ifood_challenge.data.metrics.TrackScreen
import com.brunodegan.ifood_challenge.ui.screen.favoriteMovies.events.FavoriteMoviesUiEvents
import com.brunodegan.ifood_challenge.ui.screen.favoriteMovies.state.FavoriteMoviesUiState
import com.brunodegan.ifood_challenge.ui.screen.favoriteMovies.viewModel.FavoritesViewModel
import org.koin.compose.viewmodel.koinViewModel

private const val SCREEN_NAME = "FavoriteMoviesScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteMoviesScreen(
    listState: LazyListState,
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
    onNavigateUp: () -> Unit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FavoritesViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BackHandler {
        onNavigateUp()
    }

    LaunchedEffect(Unit) {
        viewModel.getFavoriteMovies()
    }

    ObserveAsEvent(events = viewModel.snackbarState) { event ->
        when (event) {
            is SnackbarUiStateHolder.SnackbarUi -> {
                onShowSnackbar(event.msg)
            }
        }
    }

    FavoritesMoviesScreen(
        state = uiState,
        scrollBehavior = scrollBehavior,
        listState = listState,
        onEvent = {
            viewModel.onUiEvent(event = it)
        },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesMoviesScreen(
    scrollBehavior: TopAppBarScrollBehavior,
    listState: LazyListState,
    state: FavoriteMoviesUiState,
    onEvent: (FavoriteMoviesUiEvents) -> Unit,
    modifier: Modifier
) {
    when (state) {
        is FavoriteMoviesUiState.Initial -> {
            TrackScreen(screenName = SCREEN_NAME)
        }

        is FavoriteMoviesUiState.Loading -> {
            LoaderUiState()
        }

        is FavoriteMoviesUiState.Success -> {
            SuccessState(
                modifier = modifier,
                listState = listState,
                scrollBehavior = scrollBehavior,
                viewData = state.viewData,
            )
        }

        is FavoriteMoviesUiState.Error -> {
            ErrorUiState(
                modifier = modifier,
                errorData = state.error,
            ) {
                onEvent(FavoriteMoviesUiEvents.OnRetryButtonClickedUiEvent)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuccessState(
    modifier: Modifier,
    listState: LazyListState,
    scrollBehavior: TopAppBarScrollBehavior,
    viewData: List<FavoriteMoviesEntity>
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
        items(viewData.size, key = { index -> viewData[index].title }) { position ->
            FavoritesMoviesCard(
                viewData = viewData[position],
            )
        }
    }
}

@Composable
fun FavoritesMoviesCard(
    viewData: FavoriteMoviesEntity,
    modifier: Modifier = Modifier
) {
    val imageRequest = ImageRequest.Builder(LocalContext.current).data(viewData.posterPath)
        .decoderFactory(SvgDecoder.Factory()).scale(Scale.FIT).crossfade(true)
        .placeholder(R.drawable.movie_icon).error(R.drawable.error_img).build()

    Card(
        colors = CardDefaults.elevatedCardColors(MaterialTheme.colorScheme.primaryContainer),
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
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(start = dimensionResource(R.dimen.double_padding))
                .wrapContentSize()
        ) {
            AsyncImage(
                model = imageRequest,
                fallback = painterResource(R.drawable.movie_icon),
                error = painterResource(R.drawable.error_img),
                contentDescription = "",
                filterQuality = FilterQuality.Low,
                modifier = Modifier
                    .size(
                        dimensionResource(R.dimen.movie_poster_size),
                        dimensionResource(R.dimen.movie_poster_size)
                    )
                    .align(Alignment.CenterHorizontally)
                    .padding(
                        top = dimensionResource(R.dimen.double_padding),
                        bottom = dimensionResource(R.dimen.base_padding)
                    )
                    .clip(PosterShape())
            )
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
                text = stringResource(R.string.movie_release_date).format(
                    viewData.releaseDate
                ),
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Medium,
                fontSize = TextUnit(
                    value = ResourcesCompat.getFloat(
                        LocalContext.current.resources,
                        R.dimen.movie_overview_font_size
                    ), type = TextUnitType.Sp
                ),
                textAlign = TextAlign.Justify,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(
                    dimensionResource(R.dimen.base_padding)
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun PopularMoviesScreenPreview() {
    FavoritesMoviesScreen(
        scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
        listState = rememberLazyListState(),
        state = FavoriteMoviesUiState.Success(
            viewData = listOf(
                FavoriteMoviesEntity(
                    id = 0,
                    title = "title",
                    posterPath = "posterPath",
                    overview = "overview",
                    lastUpdated = 1212,
                    releaseDate = "29/04/2025"
                )
            )
        ),
        onEvent = {},
        modifier = Modifier,
    )
}


