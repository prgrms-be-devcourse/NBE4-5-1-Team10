import RequireAdmin from "@/components/auth/require-admin";
import ClientPage from "./client-page";
import client from "@/lib/backend/client";

export default async function Page({
  params,
}: {
  params: {
    id: number;
  };
}) {
  const { id } = await params;

  const response = await client.GET("/api/v1/products/{productId}", {
    params: {
      path: {
        productId: id,
      },
    },
  });

  if (response.error) {
    return <div>{response["error"]["msg"]}</div>;
  }

  const data = response.data!!;
  const product = data.data!!;

  return (
    <RequireAdmin>
      <ClientPage product={product} />
    </RequireAdmin>
  );
}
