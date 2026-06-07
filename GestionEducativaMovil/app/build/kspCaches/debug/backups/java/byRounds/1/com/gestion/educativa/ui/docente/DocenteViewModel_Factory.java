package com.gestion.educativa.ui.docente;

import com.gestion.educativa.data.api.TokenManager;
import com.gestion.educativa.data.repository.DocenteRepository;
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
public final class DocenteViewModel_Factory implements Factory<DocenteViewModel> {
  private final Provider<DocenteRepository> repositoryProvider;

  private final Provider<TokenManager> tokenManagerProvider;

  public DocenteViewModel_Factory(Provider<DocenteRepository> repositoryProvider,
      Provider<TokenManager> tokenManagerProvider) {
    this.repositoryProvider = repositoryProvider;
    this.tokenManagerProvider = tokenManagerProvider;
  }

  @Override
  public DocenteViewModel get() {
    return newInstance(repositoryProvider.get(), tokenManagerProvider.get());
  }

  public static DocenteViewModel_Factory create(Provider<DocenteRepository> repositoryProvider,
      Provider<TokenManager> tokenManagerProvider) {
    return new DocenteViewModel_Factory(repositoryProvider, tokenManagerProvider);
  }

  public static DocenteViewModel newInstance(DocenteRepository repository,
      TokenManager tokenManager) {
    return new DocenteViewModel(repository, tokenManager);
  }
}
