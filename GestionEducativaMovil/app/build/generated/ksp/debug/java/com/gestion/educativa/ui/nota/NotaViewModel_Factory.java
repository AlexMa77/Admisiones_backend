package com.gestion.educativa.ui.nota;

import com.gestion.educativa.data.api.TokenManager;
import com.gestion.educativa.data.repository.NotaRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class NotaViewModel_Factory implements Factory<NotaViewModel> {
  private final Provider<NotaRepository> repositoryProvider;

  private final Provider<TokenManager> tokenManagerProvider;

  public NotaViewModel_Factory(Provider<NotaRepository> repositoryProvider,
      Provider<TokenManager> tokenManagerProvider) {
    this.repositoryProvider = repositoryProvider;
    this.tokenManagerProvider = tokenManagerProvider;
  }

  @Override
  public NotaViewModel get() {
    return newInstance(repositoryProvider.get(), tokenManagerProvider.get());
  }

  public static NotaViewModel_Factory create(Provider<NotaRepository> repositoryProvider,
      Provider<TokenManager> tokenManagerProvider) {
    return new NotaViewModel_Factory(repositoryProvider, tokenManagerProvider);
  }

  public static NotaViewModel newInstance(NotaRepository repository, TokenManager tokenManager) {
    return new NotaViewModel(repository, tokenManager);
  }
}
