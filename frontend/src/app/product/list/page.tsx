import ClientPage from "./client-page";
import client from "@/lib/backend/client";

export default async function Page({
  searchParams,
}: {
  searchParams: {
    keyword: string;
    pageSize: number;
    page: number;
  };
}) {
  const { keyword = "", pageSize = 10, page = 1 } = await searchParams;

  const response = await client.GET("/api/v1/products", {
    params: {
      //   query: {
      //     keyword,
      //     pageSize,
      //     page,
      //   },
    },
  });

  const data = response.data!!;

  return (
    <ClientPage data={data} page={page} pageSize={pageSize} keyword={keyword} />
  );
}
